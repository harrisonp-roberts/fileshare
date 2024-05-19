window.addEventListener('DOMContentLoaded', () => {
    let dropArea = document.getElementById('drop_zone');
    dropArea.addEventListener('dragover', dragOverHandler, false)
    dropArea.addEventListener('drop', dropHandler, false)

    const conditionalDivs = Array.from(document.getElementsByClassName('conditional-display'));
    const filesToUpload = [];
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
                    const file = item.getAsFile();
                    addFileToUpload(file);
                    console.log(`… file[${i}].name = ${file.name}`);
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
    }

    function addFileToUpload(file) {
        console.log('adding file to upload')
        console.log('checking file', file);
        console.log('against list of files to upload', filesToUpload);
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
        if(selectedFiles.children.length === 0) {
            console.log('hiding components')
            conditionalDivs.forEach(div => {div.classList.add('d-none')})
        } else {
            console.log('not hiding components')
            conditionalDivs.forEach(div => {div.classList.remove('d-none')})
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
        let file = filesToUpload.indexOf(item => {
            console.log('is item same?');
            console.log(item);
            console.log(filename);
            return item.name === filename
        });

        filesToUpload.splice(file, 1);
        console.log(file);
        console.log(filesToUpload);
        refreshView();
    }
});

