package dev.hroberts.fileshare.fileupload.controllers.resources;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import reactor.util.annotation.NonNull;

import java.io.*;
import java.nio.file.Path;

public class DeletableFileSystemResource extends FileSystemResource {
    private final boolean deleteOnClose;

    public DeletableFileSystemResource(Path path, boolean deleteOnClose) {
        super(path);
        this.deleteOnClose = deleteOnClose;
    }

    @NonNull
    @Override
    public InputStream getInputStream() throws IOException {
        return new DeletableFileSystemInputStream(super.getFile(), deleteOnClose);
    }

    private static final class DeletableFileSystemInputStream extends FileInputStream {
        private final File file;
        private final boolean deleteOnClose;

        public DeletableFileSystemInputStream(File file, boolean deleteOnClose) throws FileNotFoundException {
            super(file);
            this.file = file;
            this.deleteOnClose = deleteOnClose;
        }

        @Override
        public void close() throws IOException {
            super.close();
            if (deleteOnClose) {
                FileUtils.deleteDirectory(new File(file.getParent()));
            }
        }
    }
}
