package br.com.rafaelvieira.shopbeer.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Component
public interface PhotoStorage {

    public final String THUMBNAIL_PREFIX = "thumbnail.";
    public String save(MultipartFile[] files);
    public byte[] recover(String photo);
    public byte[] recoverThumbnail(String photoBeer);
    public void delete(String photo);
    public String getUrl(String photo);
    default String renameFile(String originName) {
        return UUID.randomUUID().toString() + "_" + originName;
    }
}
