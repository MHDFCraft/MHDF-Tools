package cn.ChengZhiYa.MHDFTools.util.download.exception;

public final class ResourceUtil extends Exception {
    public ResourceUtil(String message) {
        super(message);
    }

    public ResourceUtil(Throwable cause) {
        super(cause);
    }

    public ResourceUtil(String message, Throwable cause) {
        super(message, cause);
    }
}
