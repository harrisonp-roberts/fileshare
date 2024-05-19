window.addEventListener('DOMContentLoaded', () => {
    const chunkSize = (1024 * 1024 * 50);

    let dropArea = document.getElementById('drop_zone');

    dropArea.addEventListener('dragover', dragOverHandler, false)
    dropArea.addEventListener('drop', dropHandler, false)

    let dirty = false;
    const conditionalDivs = Array.from(document.getElementsByClassName('conditional-display'));
    const filesToUpload = Array.from([]);
    const selectedFiles = document.getElementById('selected-files');
    let queue = [];

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
        doUpload();
    }

    function addFileToUpload(file) {
        console.log('adding file to upload')
        console.log('checking file', file);
        console.log('against list of files to upload', filesToUpload);

        //todo allow multi file uploads at some point
        if (filesToUpload.length > 0) {
            return;
        }

        if (filesToUpload.some(item => item.name === file.name)) {
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
        if (selectedFiles.children.length === 0) {
            console.log('hiding components')
            conditionalDivs.forEach(div => {
                div.classList.add('d-none')
            })
        } else {
            console.log('not hiding components')
            conditionalDivs.forEach(div => {
                div.classList.remove('d-none')
            })
            if (!dirty) {
                console.log(filesToUpload[0].name)
                document.getElementById('upload-name').value = filesToUpload[0].name;
            }
        }
    }

    function addSelectedFilesToView(filename) {
        let listItem = document.createElement('li');
        let closeButton = document.createElement("button");

        listItem.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-center")
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
        if (item != null) {
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

     async function complete(uploadId) {
        return await fetch('http://localhost:8080/files/complete/' + uploadId, {
            method: 'POST'
        }).then(response => response.json());
    }

    async function initiate() {
        let name = document.getElementById('upload-name').value;
        let size = filesToUpload[0].size;
        let limit = document.getElementById('download-limit').value;

        let response = await fetch('http://localhost:8080/files/initiate-multipart', {
            method: 'POST',
            body: JSON.stringify({
                name: name,
                size: size,
                downloadLimit: limit
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => response.json());

        return response.uploadId;
    }

    async function uploadChunk(e, uploadId, chunkNumber) {
        const formData = new FormData();
        let chunk = e.target.result;

        formData.append('uploadId', uploadId);
        formData.append('file', new Blob([chunk]));
        formData.append('position', chunkNumber);
        formData.append('size', chunk.byteLength);

        console.log('chunk', e.target.result.byteLength);

        await fetch('http://localhost:8080/files/upload/' + uploadId, {
            method: 'POST',
            body: formData,
            headers: {
                'Upload-Id': uploadId,
                'Chunk-Number': chunkNumber
            }
        });

        queue.splice(queue.indexOf(chunkNumber), 1);
        console.log('uploading chunk with id', uploadId);
    }

    async function doUpload() {
        //todo calculate num chunks
        let file = filesToUpload[0];
        let size = file.size;
        let numChunks = Math.ceil(size / chunkSize);
        console.log('number of chunks in file', numChunks);


        console.log('initiating request');
        let uploadId = await initiate();

        //todo send chunks off
        let pos = 0;
        while (pos < size) {
            let slice = file.slice(pos, Math.min(pos + chunkSize, size));
            let chunkNumber = Math.floor(pos / chunkSize);
            queue.push(chunkNumber);
            let reader = new FileReader();
            reader.onload = (e) => uploadChunk(e, uploadId, chunkNumber);
            reader.readAsArrayBuffer(slice);

            //2 parallel streams
            while(queue.length > 1) {
                await new Promise((r => setTimeout(r,100)));
            }

            pos += chunkSize;
            console.log('progress', (pos / size) * 100);
        }

        while (queue.length > 0) {
            console.log('queued chunks', queue);
            await new Promise(r => setTimeout(r, 200));
        }
        await new Promise(r => setTimeout(r, 10000));

        let response = await complete(uploadId);
        let downloadLink = 'http://localhost:8080/files/download/' + uploadId;
        alert('your download link: ' + downloadLink);
        console.log(response);
    }
});

