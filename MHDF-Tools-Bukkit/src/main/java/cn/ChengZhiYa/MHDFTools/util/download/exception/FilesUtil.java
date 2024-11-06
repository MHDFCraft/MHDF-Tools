package cn.ChengZhiYa.MHDFTools.util.download.exception;

public final class FilesUtil extends Exception {
    public FilesUtil(String message) {
        super(message);
    }

    public FilesUtil(Throwable cause) {
        super(cause);
    }

    public FilesUtil(String message, Throwable cause) {
        super(message, cause);
    }
}
