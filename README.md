# (Unnamed) File Sharing Application

## Important Todos:

- Gotta figure out the shareFileInfoDto => ShareFileInfo situation. The SharedFileInfo contains a filename, and a
  filepath. When uploading a file initially, the filename is extracted from the filepath. But anyway, if we were to map
  domain => dto => domain we would end up with the original filename being lost!
- It's because we shouldn't be creating the file name as the file path. That should be provided a different way because
  files are not normally uploaded by filepath, they're uploaded through http