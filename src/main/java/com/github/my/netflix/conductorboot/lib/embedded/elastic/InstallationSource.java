package com.github.my.netflix.conductorboot.lib.embedded.elastic;

import java.net.URL;

interface InstallationSource {
    String determineVersion();

    URL resolveDownloadUrl();
}

