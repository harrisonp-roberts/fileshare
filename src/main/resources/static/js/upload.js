/*
TODO:
- Allow multiple file upload (the foundation is there)
- Multiple upload streams at once
- Better UX for finishing
- Takes a long time to complete large file upload (to put pieces together).
-   Maybe look at concatenating the parts as they get uploaded? Or show user it's completed earlier or something
 */

const states = {
    SELECT: "select",
    READY: "ready",
    UPLOAD: "upload"
}

class view {
    constructor(visible, hidden) {
        this.visible = visible;
        this.hidden = hidden;
    }
}

window.addEventListener('DOMContentLoaded', () => {
    const chunkSize = (1024 * 1024 * 50);
    const baseUrl = host + "/files/";
    console.log("Base URL: ", baseUrl);
    const initialState = states.SELECT;

    let filesToUpload;
    let selectedFiles;
    let dropArea;
    let submitButton;
    let selectButton;
    let state;
    let dirty;

    let selectView;
    let readyView;
    let uploadView;
    let completeView;

    init();

    function init() {
        filesToUpload = Array.from([]);
        selectedFiles = document.getElementById('selected-files');
        dropArea = document.getElementById('drop_zone');
        submitButton = document.getElementById('submit');
        selectButton = document.getElementById('select-file');
        dirty = false;

        let uploadInformation = document.getElementById('upload-information');
        let submitElement = document.getElementById('submit-element');
        let progressElement = document.getElementById('progress-element');
        let instructionsElement = document.getElementById('instructions-column');
        let footer = document.getElementById('footer');
        let dropContainer = document.getElementById('drop-container');

        selectView = new view([dropContainer, instructionsElement], [footer, uploadInformation, submitElement, progressElement])
        readyView = new view([dropContainer, footer, instructionsElement, uploadInformation, submitElement], [progressElement]);
        uploadView = new view([dropContainer, footer, uploadInformation, uploadInformation, progressElement], [submitElement])

        dropArea.addEventListener('dragover', dragOverHandler, false)
        dropArea.addEventListener('drop', dropHandler, false)
        submitButton.addEventListener('click', submit, false);
        selectButton.addEventListener('click', openFilePicker, false);
        setState(initialState);
    }

    function openFilePicker() {
        let input = document.createElement('input');
        input.type = 'file';
        input.onchange = _ => {
            // you can use this method to get file and perform respective operations
            let files = Array.from(input.files);
            addFileToUpload(files[0]);
        };
        input.click();
    }

    function setView(view) {
        view.visible.forEach(element => {
            element.classList.remove('d-none')
        });
        view.hidden.forEach(element => {
            element.classList.add('d-none')
        });
    }

    function setState(newState) {
        if (state === newState) return;
        //todo validate state transitions...
        if (newState === states.SELECT) {
            setView(selectView);
            state = states.SELECT;
        } else if (newState === states.READY) {
            setView(readyView);
            state = states.READY;
        } else if (newState === states.UPLOAD) {
            setView(uploadView);
            state = states.UPLOAD;
        } else if (newState === states.COMPLETE) {
            setView(completeView);
            state = states.COMPLETE;
        }
    }

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
            console.log(ev.dataTransfer.items[0]);
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
    }

    function addFileToUpload(file) {
        //todo allow multi file uploads at some point
        if (filesToUpload.length > 0) {
            return;
        }

        if (filesToUpload.some(item => item.name === file.name)) {
            return;
        }
        filesToUpload.push(file);
        addSelectedFilesToView(file.name);

        setState(states.READY);
    }

    function updateProgress(percent) {
        let bar = document.getElementById('progress-bar');
        bar.ariaValueNow = percent;
        bar.style.width = `${percent}%`;
        console.log('updating with percent', percent);
    }

    //todo get rid of this and render directly from selected files.
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

        //if user hasn't set a name, and adding first file to list
        if (!dirty && selectedFiles.children.length === 1) {
            document.getElementById('upload-name').value = filename;
        }
    }

    function removeFileFromUpload(filename) {
        console.log('removing file', filename)
        let item = document.getElementById(filename);
        if (item != null) {
            console.log('removing dom element');
            item.remove();
        }

        let file = filesToUpload.find(item => item.name === filename);
        let index = filesToUpload.indexOf(file);
        filesToUpload.splice(index, 1);
    }

    async function submit() {
        setState(states.UPLOAD);

        const file = filesToUpload[0];
        const id = await initiate();
        await doUpload(id, file);
        await complete(id);

        window.location.href = host + "/complete/" + id;
    }

    function getParams() {
        let params = {
            downloadLimit: 1,
            fileName: filesToUpload[0].name
        };

        if (dirty) {
            params.fileName = document.getElementById('upload-name').value;
            params.downloadLimit = document.getElementById('download-limit').value;
        }
        return params;
    }

    async function initiate() {
        let params = getParams();
        let size = filesToUpload[0].size;

        let response = await fetch(baseUrl + 'initiate-multipart', {
            method: 'POST',
            body: JSON.stringify({
                name: params.fileName,
                size: size,
                downloadLimit: params.downloadLimit
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => response.json());

        return response.id;
    }

    async function doUpload(id, file) {
        let pos = 0;
        while (pos < file.size) {
            let slice = file.slice(pos, Math.min(pos + chunkSize, file.size));
            let chunkIndex = Math.floor(pos / chunkSize);
            let formData = buildFormData(slice, chunkIndex);

            await fetch(baseUrl + 'upload/' + id, {
                method: 'POST',
                body: formData
            });

            pos += slice.size;
            updateProgress(Math.floor((pos / file.size) * 100).toString());
        }
    }

    function buildFormData(slice, chunkIndex) {
        const formData = new FormData();
        formData.append('file', slice);
        formData.append('chunkIndex', chunkIndex);
        formData.append('size', slice.size);
        return formData;
    }

    async function complete(id) {
        return await fetch(baseUrl + 'complete/' + id, {
            method: 'PUT'
        }).then(response => response.json());
    }
});

