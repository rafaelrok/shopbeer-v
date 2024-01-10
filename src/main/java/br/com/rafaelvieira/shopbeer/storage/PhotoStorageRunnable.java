package br.com.rafaelvieira.shopbeer.storage;

import br.com.rafaelvieira.shopbeer.domain.dto.PhotoDTO;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

public class PhotoStorageRunnable implements Runnable {

    private final MultipartFile[] files;
    private final DeferredResult<PhotoDTO> result;
    private final PhotoStorage photoStorage;

    public PhotoStorageRunnable(MultipartFile[] files, DeferredResult<PhotoDTO> result, PhotoStorage photoStorage) {
        this.files = files;
        this.result = result;
        this.photoStorage = photoStorage;
    }

    @Override
    public void run() {
        String namePhoto = this.photoStorage.save(files);
        String contentType = files[0].getContentType();
        result.setResult(new PhotoDTO(namePhoto, contentType, photoStorage.getUrl(namePhoto)));
    }
}
