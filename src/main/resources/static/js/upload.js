/*
TODO:
- Allow multiple file upload (the foundation is there)
 */
// Configuration for parallel uploads
var MD5 = function(d){var r = M(V(Y(X(d),8*d.length)));return r.toLowerCase()};function M(d){for(var _,m="0123456789ABCDEF",f="",r=0;r<d.length;r++)_=d.charCodeAt(r),f+=m.charAt(_>>>4&15)+m.charAt(15&_);return f}function X(d){for(var _=Array(d.length>>2),m=0;m<_.length;m++)_[m]=0;for(m=0;m<8*d.length;m+=8)_[m>>5]|=(255&d.charCodeAt(m/8))<<m%32;return _}function V(d){for(var _="",m=0;m<32*d.length;m+=8)_+=String.fromCharCode(d[m>>5]>>>m%32&255);return _}function Y(d,_){d[_>>5]|=128<<_%32,d[14+(_+64>>>9<<4)]=_;for(var m=1732584193,f=-271733879,r=-1732584194,i=271733878,n=0;n<d.length;n+=16){var h=m,t=f,g=r,e=i;f=md5_ii(f=md5_ii(f=md5_ii(f=md5_ii(f=md5_hh(f=md5_hh(f=md5_hh(f=md5_hh(f=md5_gg(f=md5_gg(f=md5_gg(f=md5_gg(f=md5_ff(f=md5_ff(f=md5_ff(f=md5_ff(f,r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+0],7,-680876936),f,r,d[n+1],12,-389564586),m,f,d[n+2],17,606105819),i,m,d[n+3],22,-1044525330),r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+4],7,-176418897),f,r,d[n+5],12,1200080426),m,f,d[n+6],17,-1473231341),i,m,d[n+7],22,-45705983),r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+8],7,1770035416),f,r,d[n+9],12,-1958414417),m,f,d[n+10],17,-42063),i,m,d[n+11],22,-1990404162),r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+12],7,1804603682),f,r,d[n+13],12,-40341101),m,f,d[n+14],17,-1502002290),i,m,d[n+15],22,1236535329),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+1],5,-165796510),f,r,d[n+6],9,-1069501632),m,f,d[n+11],14,643717713),i,m,d[n+0],20,-373897302),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+5],5,-701558691),f,r,d[n+10],9,38016083),m,f,d[n+15],14,-660478335),i,m,d[n+4],20,-405537848),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+9],5,568446438),f,r,d[n+14],9,-1019803690),m,f,d[n+3],14,-187363961),i,m,d[n+8],20,1163531501),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+13],5,-1444681467),f,r,d[n+2],9,-51403784),m,f,d[n+7],14,1735328473),i,m,d[n+12],20,-1926607734),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+5],4,-378558),f,r,d[n+8],11,-2022574463),m,f,d[n+11],16,1839030562),i,m,d[n+14],23,-35309556),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+1],4,-1530992060),f,r,d[n+4],11,1272893353),m,f,d[n+7],16,-155497632),i,m,d[n+10],23,-1094730640),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+13],4,681279174),f,r,d[n+0],11,-358537222),m,f,d[n+3],16,-722521979),i,m,d[n+6],23,76029189),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+9],4,-640364487),f,r,d[n+12],11,-421815835),m,f,d[n+15],16,530742520),i,m,d[n+2],23,-995338651),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+0],6,-198630844),f,r,d[n+7],10,1126891415),m,f,d[n+14],15,-1416354905),i,m,d[n+5],21,-57434055),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+12],6,1700485571),f,r,d[n+3],10,-1894986606),m,f,d[n+10],15,-1051523),i,m,d[n+1],21,-2054922799),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+8],6,1873313359),f,r,d[n+15],10,-30611744),m,f,d[n+6],15,-1560198380),i,m,d[n+13],21,1309151649),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+4],6,-145523070),f,r,d[n+11],10,-1120210379),m,f,d[n+2],15,718787259),i,m,d[n+9],21,-343485551),m=safe_add(m,h),f=safe_add(f,t),r=safe_add(r,g),i=safe_add(i,e)}return Array(m,f,r,i)}function md5_cmn(d,_,m,f,r,i){return safe_add(bit_rol(safe_add(safe_add(_,d),safe_add(f,i)),r),m)}function md5_ff(d,_,m,f,r,i,n){return md5_cmn(_&m|~_&f,d,_,r,i,n)}function md5_gg(d,_,m,f,r,i,n){return md5_cmn(_&f|m&~f,d,_,r,i,n)}function md5_hh(d,_,m,f,r,i,n){return md5_cmn(_^m^f,d,_,r,i,n)}function md5_ii(d,_,m,f,r,i,n){return md5_cmn(m^(_|~f),d,_,r,i,n)}function safe_add(d,_){var m=(65535&d)+(65535&_);return(d>>16)+(_>>16)+(m>>16)<<16|65535&m}function bit_rol(d,_){return d<<_|d>>>32-_}

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
    const maxConcurrentUploads = 10; // Maximum number of concurrent chunk uploads
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
        // if (!dirty && selectedFiles.children.length === 1) {
        //     document.getElementById('upload-name').value = filename;
        // }
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
            downloadLimit: -1,
            fileName: filesToUpload[0].name
        };

        if(document.getElementById('download-limit').value > 0)
            params.downloadLimit = document.getElementById('download-limit').value;

        return params;
    }

    async function initiate() {
        let params = getParams();

        let response = await fetch(baseUrl + 'initiate-multipart', {
            method: 'POST',
            body: JSON.stringify({
                name: params.fileName,
                downloadLimit: params.downloadLimit
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => response.json());

        return response.id;
    }

    async function doUpload(id, file) {
        // Create array of chunk positions to process
        const chunks = [];
        for (let pos = 0; pos < file.size; pos += chunkSize) {
            chunks.push(pos);
        }

        // Track overall progress
        let completedChunks = 0;
        const totalChunks = chunks.length;

        // Process chunks in parallel with limited concurrency
        async function processChunks() {
            // Use Promise.all with a limited batch size
            while (chunks.length > 0) {
                const batch = chunks.splice(0, maxConcurrentUploads);
                await Promise.all(batch.map(async (pos) => {
                    const slice = file.slice(pos, Math.min(pos + chunkSize, file.size));
                    console.log(`Processing chunk at position ${pos}`);

                    const hash = await hashBlob(slice);
                    const chunkIndex = Math.floor(pos / chunkSize);
                    const formData = buildFormData(slice, chunkIndex, hash);

                    await fetch(baseUrl + 'upload/' + id, {
                        method: 'POST',
                        body: formData
                    });

                    // Update progress after each chunk completes
                    completedChunks++;
                    updateProgress(Math.floor((completedChunks / totalChunks) * 100));
                }));
            }
        }

        await processChunks();
    }

    async function hashBlob(blob) {
        // Use a more efficient approach for larger files
        try {
            const buffer = await blob.arrayBuffer(); // Read blob content as binary
            const hashBuffer = await crypto.subtle.digest("SHA-256", buffer); // Hash it
            // Convert hash to hex string
            return [...new Uint8Array(hashBuffer)]
                .map(b => b.toString(16).padStart(2, '0'))
                .join('');
        } catch (error) {
            console.error('Error generating hash:', error);
            throw error;
        }
    }

    function buildFormData(slice, chunkIndex, hash) {
        const formData = new FormData();
        formData.append('file', slice);
        formData.append('hash', hash);
        formData.append('chunkIndex', chunkIndex);
        formData.append('hashAlgorithm', 'SHA_256')
        return formData;
    }

    async function complete(id) {
        return await fetch(baseUrl + 'complete/' + id, {
            method: 'PUT'
        }).then(response => response.json());
    }
});

