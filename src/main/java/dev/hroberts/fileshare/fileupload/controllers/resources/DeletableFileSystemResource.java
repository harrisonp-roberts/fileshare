package dev.hroberts.fileshare.fileupload.controllers.resources;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import reactor.util.annotation.NonNull;

import java.io.*;
import java.nio.file.Path;

public class DeletableFileSystemResource extends FileSystemResource {

    public DeletableFileSystemResource(Path path, boolean deleteOnClose) {
        super(path);
    }

    @NonNull
    @Override
    public InputStream getInputStream() throws IOException {
        return new DeletableFileSystemInputStream(super.getFile());
    }

    private static final class DeletableFileSystemInputStream extends FileInputStream {
        private final File file;

        public DeletableFileSystemInputStream(File file) throws FileNotFoundException {
            super(file);
            this.file = file;
            //todo create random access temp file to read from or whatever.
        }

        @Override
        public void close() throws IOException {
            super.close();
            //todo delete random access temp file or something
        }
    }
}
