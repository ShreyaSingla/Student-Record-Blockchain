package Includes;

import java.util.*;
import java.io.*;

public class CRF extends sha256 {

    // Stores the output size of the function Fn()
    public int outputsize;

    public CRF(int size) {
        outputsize = size;
        assert outputsize <= 64;
    }

    // Outputs the mapped outputSize characters long string s' for an input string s
    public String Fn(String s) {
        String shasum = encrypt(s);
        return shasum.substring(0,outputsize);
    }
}
