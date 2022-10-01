package net.safety.alerts.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.safety.alerts.dto.PersonDto;
import net.safety.alerts.dto.UrlChildAlertDto;
import net.safety.alerts.exceptions.AddressNotFoundException;
import net.safety.alerts.service.UrlService;

@ExtendWith(MockitoExtension.class)
public class UrlChildAlertControllerTest {

	@Mock
	private UrlService urlService;

	@InjectMocks
	private UrlChildAlertController controllerUnderTest;

	@Test
	public void childAlertTest() throws AddressNotFoundException {
		// Arrange
		UrlChildAlertDto dto = new UrlChildAlertDto();
		List<PersonDto> children = new ArrayList<>();
		PersonDto child = new PersonDto();
		child.setFirstName("child");
		child.setLastName("one");
		children.add(child);
		dto.setChildren(children);

		List<PersonDto> otherHouseHoldMembers = new ArrayList<>();
		PersonDto member = new PersonDto();
		member.setFirstName("Other HouseHold");
		member.setLastName("Member");
		otherHouseHoldMembers.add(member);
		dto.setOtherHouseHoldMembers(otherHouseHoldMembers);

		when(urlService.urlChildAlert(anyString())).thenReturn(dto);

		// Act
		ResponseEntity<UrlChildAlertDto> result = controllerUnderTest.childAlert("address");

		// Assert
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(dto);

	}

}
