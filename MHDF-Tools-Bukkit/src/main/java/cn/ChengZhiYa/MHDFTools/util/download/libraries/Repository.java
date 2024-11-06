/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package cn.ChengZhiYa.MHDFTools.util.download.libraries;

import cn.ChengZhiYa.MHDFTools.util.download.exception.DownloadUtil;
import lombok.Getter;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;

import static cn.ChengZhiYa.MHDFTools.util.network.HttpUtil.downloadFile;
import static cn.ChengZhiYa.MHDFTools.util.network.HttpUtil.openConnection;

@Getter
public enum Repository {
    MAVEN_CENTRAL_MIRROR("aliyun", "https://maven.aliyun.com/repository/public/"),
    CODE_MC("codemc", "https://repo.codemc.io/repository/maven-public/"),
    JITPACK("jitpack", "https://jitpack.io/");

    private final String url;
    private final String id;

    Repository(String id, String url) {
        this.url = url;
        this.id = id;
    }

    public static Repository getByID(String id) {
        for (Repository repository : values()) {
            if (id.equals(repository.id)) {
                return repository;
            }
        }
        return null;
    }

    public void download(Dependency dependency, Path file) {
        try {
            URLConnection connection = openConnection(this.url + dependency.getMavenRepoPath());
            downloadFile(connection, file);
        } catch (DownloadUtil e) {
            throw new RuntimeException("下载发生错误: ", e);
        } catch (IOException e) {
            throw new RuntimeException("写入发生错误: ", e);
        }
    }
}
