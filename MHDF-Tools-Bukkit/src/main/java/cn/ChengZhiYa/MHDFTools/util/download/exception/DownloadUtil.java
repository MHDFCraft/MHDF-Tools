package cn.ChengZhiYa.MHDFTools.util.download.exception;

public final class DownloadUtil extends Exception {
    public DownloadUtil(String message) {
        super(message);
    }

    public DownloadUtil(Throwable cause) {
        super(cause);
    }

    public DownloadUtil(String message, Throwable cause) {
        super(message, cause);
    }
}
