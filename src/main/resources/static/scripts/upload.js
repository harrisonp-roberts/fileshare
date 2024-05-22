/*
TODO:
- Tidy up this javascript, maybe make a state machine or something to handle view state properly
- Allow multiple file upload (the foundation is there)
- Multiple upload streams at once
- Better UX for finishing
- Takes a long time to complete large file upload (to put pieces together).
-   Maybe look at concatenating the parts as they get uploaded? Or show user it's completed earlier or something
 */

const states = {
    SELECT: "select",
    READY: "ready",
    UPLOAD: "upload",
    COMPLETE: "complete"
}

const transitions = {
    BeginUpload: {
        [states.SELECT]: states.UPLOAD
    },
    UploadComplete: {
        [states.UPLOAD]: states.COMPLETE
    },
    NewUpload: {
        [states.COMPLETE]: states.UPLOAD
    }
}

class view {
    constructor(visible, hidden) {
        this.visible = visible;
        this.hidden = hidden;
    }
}

window.addEventListener('DOMContentLoaded', () => {
    const chunkSize = (1024 * 1024 * 50);
    const baseUrl = "http://localhost:8080/files/";
    const initialState = states.SELECT;

    let conditionalDivs;
    let filesToUpload;
    let selectedFiles;
    let dropArea ;
    let submitButton;
    let state;
    let dirty;

    let selectView;
    let selectedView;
    let uploadView;
    let doneView;

    init();

    function init() {
        conditionalDivs = Array.from(document.getElementsByClassName('conditional-display'));
        filesToUpload = Array.from([]);
        selectedFiles = document.getElementById('selected-files');
        dropArea = document.getElementById('drop_zone');
        submitButton = document.getElementById('submit');
        dirty = false;

        let uploadInformation = document.getElementById('upload-information');
        let submitElement = document.getElementById('submit-element');
        let progressElement = document.getElementById('progress-element');
        let instructionsElement = document.getElementById('instructions-column');
        let footer = document.getElementById('footer');

        selectView = new view([instructionsElement], [footer, uploadInformation, submitElement, progressElement])
        selectedView = new view([footer, instructionsElement, uploadInformation, submitElement], [progressElement]);
        uploadView = new view([footer, uploadInformation, uploadInformation, progressElement], [submitElement])
        doneView = selectView;

        dropArea.addEventListener('dragover', dragOverHandler, false)
        dropArea.addEventListener('drop', dropHandler, false)
        submitButton.addEventListener('click', submit, false);
        setState(initialState);

    }

    function setView(view) {
        view.visible.forEach(element => {element.classList.remove('d-none')});
        view.hidden.forEach(element => {element.classList.add('d-none')});
    }

    function setState(newState) {
        if(state === newState) return;
        //todo validate state transitions...
        //todo bring all the names into line (ready/selected should be one. done/complete should be one)
        if(newState === states.SELECT) {
            setView(selectView);
            state = states.SELECT;
        } else if (newState === states.READY) {
            setView(selectedView);
            state = states.READY;
        } else if (newState === states.UPLOAD) {
            setView(uploadView);
            state = states.UPLOAD;
        } else if (newState === states.COMPLETE) {
            setView(doneView);
            state = states.READY;
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
        if(!dirty && selectedFiles.children.length === 1) {
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
        const uploadId = await initiate();
        await doUpload(uploadId, file);
        await complete(uploadId);

        let downloadLink = baseUrl + 'download/' + uploadId;
        setState(states.COMPLETE);
        alert('Download link: ' + downloadLink);
    }

    function getParams() {
        let params = {
            downloadLimit: 1,
            fileName: filesToUpload[0].name
        };

        if(dirty) {
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

        return response.uploadId;
    }

    async function doUpload(uploadId, file) {
        let pos = 0;
        while (pos < file.size) {
            let slice = file.slice(pos, Math.min(pos + chunkSize, file.size));
            let chunkIndex = Math.floor(pos / chunkSize);
            let formData = buildFormData(slice, chunkIndex);

            await fetch(baseUrl + 'upload/' + uploadId, {
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

    async function complete(uploadId) {
        return await fetch(baseUrl + 'complete/' + uploadId, {
            method: 'POST'
        }).then(response => response.json());
    }
});

