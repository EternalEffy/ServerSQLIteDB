public class FileInfo {
    private String fileName,path,extension,comment;

    public FileInfo(String fileName,String path,String extension,String comment){
        this.fileName=fileName;
        this.path=path;
        this.extension=extension;
        this.comment=comment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String requestAdd(){
        return "INSERT INTO 'fileInfo' ('fileName', 'path', 'extension', 'comment') VALUES ('"+fileName+"', '"+path+"', '"+extension+"', '"+comment+"'); ";
    }

}