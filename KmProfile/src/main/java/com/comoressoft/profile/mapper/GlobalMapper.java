package com.comoressoft.profile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.comoressoft.profile.dto.KmUserDTO;
import com.comoressoft.profile.model.KmUser;



@Mapper(componentModel = "spring")
public interface GlobalMapper {
	/*
	@Mappings({ @Mapping(target = "catId", source = "id"), @Mapping(target = "catLabel", source = "categoryLabel"),
			@Mapping(target = "catState", source = "categoryState"),
			@Mapping(target = "catTotalCost", source = "categoryTotalCost"),
			@Mapping(target = "subCategories", source = "subCategory") })
	CategoryDTO categoryToCategoryDTO(Category category);
*/
	@Mapping(target = "kmUserId", source = "id")
	KmUserDTO kmUserToKmUserDTO (KmUser kmUser);

	@Mapping(target = "id", source = "kmUserId")
	KmUser mmUserDTOToKmUser(KmUserDTO mmUser);

	

}
