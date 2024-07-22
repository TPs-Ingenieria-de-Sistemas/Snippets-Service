package org.example.snippetservice.domains.snippet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetStatus;
import org.example.snippetservice.domains.snippet.model.Snippet;
import org.example.snippetservice.domains.snippet.repository.SnippetRepository;
import org.example.snippetservice.domains.snippet.service.SnippetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SnippetServiceImplTests {

	@InjectMocks
	private SnippetServiceImpl snippetService;

	@Mock
	private SnippetRepository snippetRepository;

	@Mock
	private RestTemplate restTemplate;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void createSnippet_Conflict() {
		CreateSnippetDTO dto = new CreateSnippetDTO();
		dto.userId = "550e8400-e29b-41d4-a716-446655440000";
		dto.name = "Snippet Title";
		dto.content = "Snippet Content";

		when(snippetRepository.findByUserIdAndName(dto.userId, dto.name)).thenReturn(Optional.of(new Snippet()));

		ResponseEntity<SnippetDTO> response = snippetService.createSnippet(dto, false);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	@Test
	public void getSnippet_NotFound() {
		String userId = "550e8400-e29b-41d4-a716-446655440000";
		String name = "Snippet Title";

		when(snippetRepository.findByUserIdAndName(userId, name)).thenReturn(Optional.empty());

		ResponseEntity<SnippetDTO> response = snippetService.getSnippetByUserIdAndName(userId, name);

		assertNull(response.getBody());
	}

	@Test
	public void updateSnippet_NotFound() {
		Long snippetId = 1L;
		String userId = "550e8400-e29b-41d4-a716-446655440000";
		SnippetDTO dto = new SnippetDTO();
		dto.name = "Updated Title";
		dto.content = "Updated Content";

		when(snippetRepository.findById(snippetId)).thenReturn(Optional.empty());

		ResponseEntity<SnippetDTO> response = snippetService.updateSnippet(userId, dto.name, "newName", dto.content);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void deleteSnippet_NotFound() {
		String userId = "550e8400-e29b-41d4-a716-446655440000";
		String name = "Snippet Title";

		when(snippetRepository.findByUserIdAndName(userId, name)).thenReturn(Optional.empty());

		ResponseEntity<String> response = snippetService.deleteSnippet(userId, name);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void updateSnippetStatus() {
		String userId = "550e8400-e29b-41d4-a716-446655440000";
		String name = "Snippet Title";

		when(snippetRepository.findByUserIdAndName(userId, name)).thenReturn(Optional.empty());

		ResponseEntity<String> response = snippetService.updateSnippetStatus(userId, name, SnippetStatus.PENDING);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		when(snippetRepository.findByUserIdAndName(userId, name)).thenReturn(Optional.of(new Snippet()));

		response = snippetService.updateSnippetStatus(userId, name, SnippetStatus.PENDING);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void getUserSnippets() {
		String userId = "550e8400-e29b-41d4-a716-446655440000";
		String name = "Snippet Title";

		when(snippetRepository.findByUserIdAndName(userId, name)).thenReturn(Optional.empty());

		List<SnippetDTO> response = snippetService.getUserSnippets(userId);

		assertEquals(0, response.size());

		Snippet snippet = new Snippet();
		snippet.setUserId(userId);
		snippet.setName(name);
		snippet.setContent("Snippet Content");
		snippet.setLanguage("Java");

		when(snippetRepository.findAllByUserId(userId)).thenReturn(List.of(snippet));

		response = snippetService.getUserSnippets(userId);

		assertEquals(1, response.size());
	}
}
