package cn.ChengZhiYa.MHDFTools.exception;

public final class DownloadException extends Exception {
    public DownloadException(String message) {
        super(message);
    }

    public DownloadException(Throwable cause) {
        super(cause);
    }

    public DownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
