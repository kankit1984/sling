/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.commons.contentdetection.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.contentdetection.FileNameExtractor;
import org.osgi.framework.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Component(metatype = true, label = "%filenameextractor.service.name", description = "%filenameextractor.service.description")
@Service(FileNameExtractor.class)
@Property(name = Constants.SERVICE_DESCRIPTION, value = "Apache Sling Filename Extractor Service")
public class FileNameExtractorImpl implements FileNameExtractor {
    public String extract(String name) {
        // If the name is a URL, skip the trailing query and fragment parts
        int question = name.indexOf('?');
        if (question != -1) {
            name = name.substring(0, question);
        }
        int hash = name.indexOf('#');
        if (hash != -1) {
            name = name.substring(0, hash);
        }

        // If the name is a URL or a path, skip all but the last component
        int slash = name.lastIndexOf('/');
        if (slash != -1) {
            name = name.substring(slash + 1);
        }
        int backslash = name.lastIndexOf('\\');
        if (backslash != -1) {
            name = name.substring(backslash + 1);
        }

        // Decode any potential URL encoding
        int percent = name.indexOf('%');
        if (percent != -1) {
            try {
                name = URLDecoder.decode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError("UTF-8 not supported");
            }
        }

        // Skip any leading or trailing whitespace
        name = name.trim();
        return name;
    }
}
