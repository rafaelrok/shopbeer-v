package br.com.rafaelvieira.shopbeer.storage.local;

import br.com.rafaelvieira.shopbeer.storage.PhotoStorage;
import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.FileSystems.getDefault;

public class LocalPhotoStorage implements PhotoStorage {

    private static final Logger logger = LoggerFactory.getLogger(LocalPhotoStorage.class);
    private static final String THUMBNAIL_PREFIX = "thumbnail.";

    @Value("${shopbeer.photo-storage-local.local}")
    private Path local;

    @Value("${shopbeer.photo-storage-local.url-base}")
    private String urlBase;

    @Override
    public String save(MultipartFile[] files) {
        String newName = null;
        if (files != null && files.length > 0) {
            MultipartFile file = files[0];
            newName = renameFile(file.getOriginalFilename());
            try {
                file.transferTo(new File(this.local.toAbsolutePath().toString() + getDefault().getSeparator() + newName));
            } catch (IOException e) {
                throw new RuntimeException("Error saving photo", e);
            }
        }

        try {
            assert newName != null;
            Thumbnails.of(this.local.resolve(newName).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
        } catch (IOException e) {
            throw new RuntimeException("Error generating thumbnail", e);
        }

        return newName;
    }

    @Override
    public byte[] recover(String name) {
        try {
            return Files.readAllBytes(this.local.resolve(name));
        } catch (IOException e) {
            throw new RuntimeException("Error reading photo", e);
        }
    }

    @Override
    public byte[] recoverThumbnail(String photoBeer) {
        return recover(THUMBNAIL_PREFIX + photoBeer);
    }

    @Override
    public void delete(String photo) {
        try {
            Files.deleteIfExists(this.local.resolve(photo));
            Files.deleteIfExists(this.local.resolve(THUMBNAIL_PREFIX + photo));
        } catch (IOException e) {
            logger.warn(String.format("Error deleting photo '%s'. Message: %s", photo, e.getMessage()));
        }

    }

    @Override
    public String getUrl(String photo) {
        return urlBase + photo;
    }

    @PostConstruct
    private void createFolders() {
        try {
            Files.createDirectories(this.local);

            if (logger.isDebugEnabled()) {
                logger.debug("Folders created to save photos.");
                logger.debug("Folder default: " + this.local.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating folder to save photo", e);
        }
    }
}
