package k_drive.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Fservice {
	// 변수	
	String upath = "C:\\sts-bundle\\ws\\k_drive\\src\\main\\webapp\\static\\upfolder_";
	
	// 파일업로드처리
	public Map<String, String> upload(List<MultipartFile> fs)
	{	// 정보출력
		Map<String, String> map = new HashMap<>();
		for(MultipartFile f : fs)
		{				
			// 초기			
			UUID uid = UUID.randomUUID(); // 랜덤아이디 생성
			String oname = f.getOriginalFilename();
			String fname = uid.toString() + "_" + oname; // 유일한 이름
			
											
			// 파일처리
			File file = new File(upath, fname);
			try {
				f.transferTo(file);
				map.put(fname, oname);
			} catch (Exception e) {
				e.printStackTrace();			
			}			
		}		
		return map;		
	}
	
	public void uploads_and_db(List<MultipartFile> fs)
	{	// 정보출력		
		for(MultipartFile f : fs)
		{				
			// 초기			
			UUID uid = UUID.randomUUID(); // 랜덤아이디 생성
			String oname = f.getOriginalFilename();
			String fname = uid.toString() + "_" + oname; // 유일한 이름
											
			// 파일처리
			File file = new File(upath, fname);
			try {
				f.transferTo(file);
				
			} catch (Exception e) {
				e.printStackTrace();			
			}			
		}				
	}
}
