package com.iso.plogues.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MyInfoDto {
		private String userId;
		private String userName;
		private String email;
		private String phone;
		private String address;
		private String info;
		private int joinCount;
		private int plogWeight;
		private int treeCount;
		private String fileUrl;
}
