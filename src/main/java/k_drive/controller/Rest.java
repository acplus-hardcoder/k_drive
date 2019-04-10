package k_drive.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import k_drive.maps.Mappable;
import k_drive.service.FolderBean;
import k_drive.service.Fservice;

@RestController // 레스트풀서비스를 위한 컨트롤러
public class Rest {
	@Autowired Mappable km;
	
	@Autowired Fservice fs;

	@RequestMapping("/kdrive_copy")
	public List<HashMap<String, String>> kdrive_copy(int source, int target, int table)
	{		
		return km.getKdriveCopy(source, target, table);
	}
	
	@RequestMapping("/kdrive_move")
	public void kdrive_move(int source, int target, int table)
	{		
		km.kdriveMove(source, target, table);
	}
	
	@RequestMapping("/kdrive_select")
	public List<HashMap<String, String>> kdrive_select(int num, int table)
	{		
		return km.getKdriveSelect(num, table);
	}
	
	@RequestMapping("/kdrive_md")
	public List<HashMap<String, String>> kdrive_md(int num, String name, int table)
	{		
		return km.getKdriveMd(num, name, table);
	}
	
	@RequestMapping("/kdrive_del")
	public void kdrive_del(int num, int table)
	{		
		km.kdriveDel(num, table);
	}
	
	@RequestMapping(value = { "/upload_folder" }, method = RequestMethod.POST)//, consumes ={"multipart/form-data"})
	public List<HashMap<String, String>> uploadFolder(MultipartHttpServletRequest multi, int table, int target, String[] dir) 	
	{		
		List<FolderBean> fb_list = new ArrayList<>();		
		int addIndex = 0;		
		boolean path_flag = true;		
		System.out.println(target + ":" + table + ":" + dir[0]);
		
		for(int i = 0; i < dir.length; i++)
		{			
			String[] str = dir[i].split("/");						
			String path = String.valueOf(target) + "/";
						
			for(int j = 0; j < str.length-1; j++)
			{	
				path += str[j] + "/";
				for(FolderBean fi : fb_list)
				{
					if(path.equals(fi.getPath()))
					{
						path_flag = false;
						break;						
					}
					else 
					{
						path_flag = true;
					}					
				}	
				if(path_flag)
				{
					FolderBean f = new FolderBean();
					f.setPath(path);
					f.setName(str[j]);					
					fb_list.add(addIndex++, f);
				}				
			}												
		}
		for(FolderBean f : fb_list)
		{
			System.out.println("path : " + f.getPath() + "\tname : " + f.getName());
		}
	
		HashMap<String, Integer> folder_map = new HashMap<>();		
		int first_sequence_num = 1000000000;
		
		for(FolderBean f : fb_list)
		{	
			String[] str = f.getPath().split("/");
			String path = "";
			for(int i = 0; i < str.length-1; i++)
			{
				path += str[i] + "/";
			}			
			if(folder_map.containsKey(path))
			{
				f.setParent_num(folder_map.get(path));
			}
			else
			{
				f.setParent_num(target);
			}			
			String str_num = km.makeFolder(f.getParent_num(), f.getName(), table).get(0).get("S_NUM");
			f.setThis_num(Integer.parseInt(str_num));
			folder_map.put(f.getPath(), f.getThis_num());
			if(first_sequence_num > f.getThis_num())
				first_sequence_num = f.getThis_num();
		}
		
		String upload_path = String.format("C:\\sts-bundle\\ws\\k_drive\\src\\main"
				+ "\\webapp\\static\\upfolder_%s\\", table);
		List<MultipartFile> fileList = multi.getFiles("file");
		int index = 0;
		for (MultipartFile mf : fileList)
		{			
			String originFileName = mf.getOriginalFilename(); // 원본 파일 명
			UUID uid = UUID.randomUUID(); // 랜덤아이디 생성
			String safeFile = upload_path + uid;			
            try {
                mf.transferTo(new File(safeFile));
                String[] str = dir[index++].split("/");						
                String path = String.valueOf(target) + "/";    						
    			for(int j = 0; j < str.length-1; j++)
    			{	
    				path += str[j] + "/";
    			}    			
    			km.makeFile((int)folder_map.get(path), originFileName, uid.toString(), table);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }		
		return km.getKdriveUpFolder(first_sequence_num, table);
	}
	
	@RequestMapping(value = { "/upload_file" }, method = RequestMethod.POST)
	public List<HashMap<String, String>> upload_file(MultipartHttpServletRequest mfrq, int table, int target)	
	{
		List<HashMap<String, String>> return_value = new ArrayList<>();
		HashMap<String, String> return_map = new HashMap<>();
		
		List<MultipartFile> fileList = mfrq.getFiles("file");		

		String path = String.format("C:\\sts-bundle\\ws\\k_drive\\src\\main\\webapp\\static\\upfolder_%s\\", table);
				
        for (MultipartFile mf : fileList) {        	 
            String originFileName = mf.getOriginalFilename(); // 원본 파일 명            
            UUID uid = UUID.randomUUID(); // 랜덤아이디 생성            
            String safeFile = path + uid;
            try {
                mf.transferTo(new File(safeFile));
                km.makeFile(target, originFileName, uid.toString(), table);
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }            
        }
        return_map.put("result", "true");
        return_value.add(return_map);
        return return_value;
	}
}