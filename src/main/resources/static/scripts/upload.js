window.addEventListener('DOMContentLoaded', () => {
    const chunkSize = (1024 * 1024 * 10);
    let dropArea = document.getElementById('drop_zone');
    dropArea.addEventListener('dragover', dragOverHandler, false)
    dropArea.addEventListener('drop', dropHandler, false)

    let dirty = false;
    const conditionalDivs = Array.from(document.getElementsByClassName('conditional-display'));
    const filesToUpload = Array.from([]);
    const selectedFiles = document.getElementById('selected-files');

    function dragOverHandler(ev) {
        // Prevent default behavior (Prevent file from being opened)
        ev.preventDefault();
    }
    function dropHandler(ev) {
        ev.preventDefault();

        console.log('drop handler triggered');
        if (ev.dataTransfer.items) {
            console.log('ev.dataTransfer.items');
            // Use DataTransferItemList interface to access the file(s)
            [...ev.dataTransfer.items].forEach((item, i) => {
                // If dropped items aren't files, reject them
                if (item.kind === "file") {
                    console.log('file.');
                    const file = item.getAsFile();
                    addFileToUpload(file);
                    console.log(`… file[${i}].name = ${file.name}`);
                } else {
                    console.log('not file?', item);
                }
            });
        } else {
            console.log('ev.datatransfer.files');
            // Use DataTransfer interface to access the file(s)
            [...ev.dataTransfer.files].forEach((file, i) => {
                addFileToUpload(file);
                console.log(`… file[${i}].name = ${file.name}`);
            });
        }
        read();
        //doUpload();
    }

    function addFileToUpload(file) {
        console.log('adding file to upload')
        console.log('checking file', file);
        console.log('against list of files to upload', filesToUpload);

        //todo allow multi file uploads at some point
        if(filesToUpload.length > 0) {
            return;
        }

        if(filesToUpload.some(item => item.name === file.name)) {
            console.log('duplicate file found');
            return;
        }
        console.log('no duplicate file found. Adding to upload list');
        filesToUpload.push(file);
        console.log('file added to upload list. adding to view')
        console.log(filesToUpload);
        addSelectedFilesToView(file.name);
        refreshView();
    }

    function refreshView() {
        console.log('refreshing view, view should match internal array', filesToUpload)
        if(selectedFiles.children.length === 0) {
            console.log('hiding components')
            conditionalDivs.forEach(div => {div.classList.add('d-none')})
        } else {
            console.log('not hiding components')
            conditionalDivs.forEach(div => {div.classList.remove('d-none')})
            if(!dirty) {
                console.log(filesToUpload[0].name)
                document.getElementById('upload-name').value = filesToUpload[0].name;
            }
        }
    }

    function addSelectedFilesToView(filename) {
        let listItem = document.createElement('li');
        let closeButton = document.createElement("button");

        listItem.classList.add("list-group-item",  "d-flex", "justify-content-between", "align-items-center")
        listItem.id = filename;
        closeButton.classList.add('btn-close');
        closeButton.ariaLabel = 'Remove';
        closeButton.onclick = () => removeFileFromUpload(filename);

        listItem.append(document.createTextNode(filename));
        listItem.append(closeButton);

        selectedFiles.append(listItem);
        console.log('file added to view with filename', filename);
    }

    function removeFileFromUpload(filename) {
        console.log('removing file', filename)
        let item = document.getElementById(filename);
        if(item != null) {
            console.log('removing dom element');
            item.remove();
        }

        console.log('removing from internal list');
        console.log(filesToUpload);
        let file = filesToUpload.find(item => item.name === filename);
        let index = filesToUpload.indexOf(file);
        filesToUpload.splice(index, 1);
        console.log('index of item to remove', index);
        console.log('splicing array', filesToUpload)
        console.log('spliced', filesToUpload);
        console.log(file);
        console.log(filesToUpload);
        refreshView();
    }

    function uploadChunk(range) {

        let kb = file.slice(0, 10);
        let reader = new FileReader();
        reader.onload = (e) => {
            let chunk = e.target.result;
            console.log(chunk.toString());

            let enc = new TextDecoder('utf-8');
            console.log(enc.decode(chunk));
        }
        reader.readAsArrayBuffer(kb);
    }

    function doUpload() {
        //todo calculate num chunks
        let file = filesToUpload[0];
        let size = file.size;
        let numChunks = Math.ceil(size / chunkSize);

        console.log('number of chunks in file', numChunks);

        //todo initiate
        //todo send chunks off
        let pos = 0;
        while(pos < size) {
            let chunk;
            if((size - pos) >= chunkSize) {
                chunk = uploadChunk(file.slice(pos, (pos + chunkSize)));
            } else {
                chunk = uploadChunk(file.slice(pos, size));
            }
        }
    }
});

