package edu.polytech.balancetalan;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface DataProvider {
    Map<String, String> getTextData();
    Map<String, List<File>> getFiles();
}
