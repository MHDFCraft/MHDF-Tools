package cn.ChengZhiYa.MHDFTools.exception;

public final class FileException extends Exception {
    public FileException(String message) {
        super(message);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
