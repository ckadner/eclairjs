/*
 * Copyright 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.eclair;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.spark.SparkContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;

public class TestUtils {

    private static ScriptEngine engine = null;

    public static ScriptEngine getEngine() {
        if(engine == null) {
            ScriptEngineManager engineManager = new ScriptEngineManager();
            engine = engineManager.getEngineByName("nashorn");
            SparkContext sc = new SparkContext("local[*]", "testapp");
            engine.put("sc", sc);

            SparkBootstrap b = new SparkBootstrap();
            b.load(engine);
        }

        return engine;
    }

    public static String resourceToFile(String resource) throws Exception {
        URI uri = TestUtils.class.getResource(resource).toURI();
        File src = new File(uri);
        File dest = new File(System.getProperty("java.io.tmpdir"),
                FilenameUtils.getName(uri.toURL().toString()));

        FileUtils.copyFile(src, dest);

        return dest.getAbsolutePath();
    }

    public static void evalJSResource(ScriptEngine engine, String resource) throws Exception {
        engine.eval(new InputStreamReader(TestUtils.class.getResourceAsStream(resource)));
    }
}