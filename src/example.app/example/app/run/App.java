package example.app.run;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import example.lib.constant.Const;
import example.lib.util.StringUtil;


public class App {
    
    public List<String> readConfig() throws Exception{
        List<String> filenames = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/resource/config.yml")));
        String resource;

        while ((resource = br.readLine()) != null) {
            filenames.add(resource);
        }
        return filenames;
    }

    public static void main(String[] args) {

    
        System.out.println( StringUtil.lower(Const.LINK ));

        try {
            for (String line : new App().readConfig()) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    
}
