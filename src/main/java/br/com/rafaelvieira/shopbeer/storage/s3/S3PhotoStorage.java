package br.com.rafaelvieira.shopbeer.storage.s3;

import br.com.rafaelvieira.shopbeer.storage.PhotoStorage;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

//@Profile("dev")
@Component
public class S3PhotoStorage implements PhotoStorage {

    private static final Logger logger = LoggerFactory.getLogger(S3PhotoStorage.class);
    private static final String BUCKET = "aw-shopbeer-photo";
    private final AmazonS3 amazonS3;

    public S3PhotoStorage(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String save(MultipartFile[] files) {
        String newName = null;
        if (files != null && files.length > 0) {
            MultipartFile file = files[0];
            newName = renameFile(file.getOriginalFilename());

            try {
                AccessControlList acl = new AccessControlList();
                acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

                sendPhoto(newName, file, acl);
                sendThumbnail(newName, file, acl);
            } catch (IOException e) {
                throw new RuntimeException("Error saving file to S3", e);
            }
        }

        return newName;
    }

    @Override
    public byte[] recover(String foto) {
        InputStream is = amazonS3.getObject(BUCKET, foto).getObjectContent();
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            logger.error("Unable to retrieve photo of S3", e);
        }
        return null;
    }

    @Override
    public byte[] recoverThumbnail(String photo) {
        return recover(PhotoStorage.THUMBNAIL_PREFIX + photo);
    }

    @Override
    public void delete(String photo) {
        amazonS3.deleteObjects(new DeleteObjectsRequest(BUCKET).withKeys(photo, THUMBNAIL_PREFIX + photo));
    }

    @Override
    public String getUrl(String photo) {
        if (!StringUtils.hasText(photo)) {
            return "https://s3-us-west-1.amazonaws.com/aw-shopbeer-photo/" + photo;
        }

        return null;
    }

    private void sendPhoto(String newName, MultipartFile file, AccessControlList acl)
            throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(new PutObjectRequest(BUCKET, newName, file.getInputStream(), metadata)
                .withAccessControlList(acl));
    }

    private void sendThumbnail(String novoNome, MultipartFile arquivo, AccessControlList acl)	throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(arquivo.getInputStream()).size(40, 68).toOutputStream(os);
        byte[] array = os.toByteArray();
        InputStream is = new ByteArrayInputStream(array);
        ObjectMetadata thumbMetadata = new ObjectMetadata();
        thumbMetadata.setContentType(arquivo.getContentType());
        thumbMetadata.setContentLength(array.length);
        amazonS3.putObject(new PutObjectRequest(BUCKET, THUMBNAIL_PREFIX + novoNome, is, thumbMetadata)
                .withAccessControlList(acl));
    }

}
