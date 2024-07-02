const states = {
    SELECT: "select",
    DOWNLOAD: "download",
    COMPLETE: "complete"
}

class view {
    constructor(visible, hidden) {
        this.visible = visible;
        this.hidden = hidden;
    }
}

window.addEventListener('DOMContentLoaded', () => {
    const baseUrl = "http://localhost:8080/files/";
    let fileInfoUrl = baseUrl + "info/";
    let downloadUrl = baseUrl + "download/";

    const initialState = states.SELECT;

    let submitButton;
    let state;

    let fileId;

    let selectView;
    let downloadView;
    let completeView;

    init();

    function init() {
        submitButton = document.getElementById('select-file');
        const urlParams = new URLSearchParams(window.location.search);
        let urlFileId = urlParams.get('fileId');

        let uploadRow = document.getElementById("upload-row");
        let downloadRow = document.getElementById("download-row");

        selectView = new view([uploadRow], [downloadRow]);
        downloadView = new view([downloadRow], [uploadRow]);

        submitButton.addEventListener('click', submit, false);

        if(urlFileId) {
            console.log("Setting download state");
            fileId = urlFileId;
            setState(states.DOWNLOAD)
            doDownload().then(res => setState(states.COMPLETE));
        } else {
            console.log("Setting select state");
            setState(states.SELECT);
        }

        console.log("State", state);
    }

    function grabUUID(url) {
        //todo fix dirty
        console.log(url);
        let parts = url.split("/");
        let uuid = parts[parts.length - 1];
        console.log(uuid.length);
        if(uuid.length === 36) return uuid;
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
        } else if (newState === states.DOWNLOAD) {
            setView(downloadView);
            state = states.DOWNLOAD;
        } else if (newState === states.COMPLETE) {
            setView(completeView);
            state = states.COMPLETE;
        }
    }

    async function doDownload() {
        await getFileInfo();
        await downloadFile();
    }

    function submit() {
        let inputBox = document.getElementById('download-link');
        fileId = grabUUID(inputBox.value);
        if(!fileId) return;
        setState(states.DOWNLOAD);
        doDownload().then(res => setState(states.COMPLETE));
    }

    async function getFileInfo() {
        var remainingDownloadsComponent = document.getElementById("remaining-downloads");
        var nameComponent = document.getElementById("file-title");

        await fetch(fileInfoUrl + fileId).then(res => res.json()).then(json => {
            console.log("Setting info", json);
            nameComponent.textContent = "Downloading: " + json.fileName;
            remainingDownloadsComponent.textContent = "Remaining Downloads: " + json.downloadLimit;
        });
    }

    async function downloadFile() {
        document.getElementById('my_iframe').src = downloadUrl + fileId;
    }

});

