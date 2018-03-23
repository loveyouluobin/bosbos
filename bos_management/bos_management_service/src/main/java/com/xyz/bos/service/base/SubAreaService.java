package com.xyz.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xyz.bos.domain.base.SubArchive;
import com.xyz.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 上午9:40:28 <br/>       
 */
public interface SubAreaService {


    Page<SubArea> findAll(Pageable pageable);

	void save(SubArea model);

	List<SubArea> findUnAssociatedSubAreas();

	List<SubArea> findAssociatedSubAreas(Long id);




  
}
  
