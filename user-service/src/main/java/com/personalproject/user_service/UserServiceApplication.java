package com.personalproject.user_service;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//		mapper.typeMap(MedicalRecordAddDTO.class, MedicalRecord.class)
//				.addMapping(MedicalRecordAddDTO::getPatientId, MedicalRecord::setUserId);
//		mapper.typeMap(MedicalRecord.class, MedicalRecordAddDTO.class)
//				.addMapping(MedicalRecord::getUserId, MedicalRecordAddDTO::setPatientId);
//		mapper.typeMap(Medicine.class, MedicineInfoDTO.class)
//				.addMapping((medicine) -> medicine.getMedicineCategory().getMedicineCategoryId(), MedicineInfoDTO::setMedicineCategoryId)
//				.addMapping((medicine) -> medicine.getMedicineCategory().getMedicineCategoryName(), MedicineInfoDTO::setMedicineCategoryName);
//
//		Converter<Integer, MedicalRecord> specialtyConverter = ctx -> {
//			if (ctx.getSource() == null) return null;
//			MedicalRecord s = new MedicalRecord();
//			s.setMedicalRecordId(ctx.getSource());
//			return s;
//		};
//
//		mapper.typeMap(ExaminationInfoAddDTO.class, Examination.class)
//				.addMappings(m -> m.using(specialtyConverter)
//						.map(ExaminationInfoAddDTO::getMedicalRecordId, Examination::setMedicalRecord));

		return mapper;
	}

}
