package org.example.snippetservice.domains.snippet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.example.snippetservice.domains.snippet.controller.SnippetController;
import org.example.snippetservice.domains.snippet.dto.*;
import org.example.snippetservice.domains.snippet.service.SnippetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SnippetControllerTest {

    @InjectMocks
    private SnippetController snippetController;

    @Mock
    private SnippetService snippetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSnippet() {
        CreateSnippetDTO createSnippetDTO = new CreateSnippetDTO();
        createSnippetDTO.userId = UUID.randomUUID();
        createSnippetDTO.name = "testSnippet";
        createSnippetDTO.content = "snippet content";
        createSnippetDTO.language = "plaintext";

        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.id = 1L;
        snippetDTO.userId = createSnippetDTO.userId;
        snippetDTO.name = "testSnippet";
        snippetDTO.content = "snippet content";
        snippetDTO.language = "plaintext";

        when(snippetService.createSnippet(createSnippetDTO, false))
                .thenReturn(new ResponseEntity<>(snippetDTO, HttpStatus.CREATED));

        ResponseEntity<SnippetDTO> response = snippetController.storeSnippet(createSnippetDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testSnippet", response.getBody().name);
    }

    @Test
    public void testGetSnippet() {
        String content = "snippet content";
        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.id = 1L;
        snippetDTO.userId = UUID.randomUUID();
        snippetDTO.name = "testSnippet";
        snippetDTO.content = content;

        when(snippetService.getSnippetByUserIdAndName(snippetDTO.userId, "testSnippet"))
                .thenReturn(new ResponseEntity<>(snippetDTO, HttpStatus.OK));

        ResponseEntity<SnippetDTO> response = snippetController.getSnippet(snippetDTO.userId, "testSnippet");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("snippet content", Objects.requireNonNull(response.getBody()).content);
    }

    @Test
    public void testUpdateSnippet() {
        UpdateSnippetDTO updateSnippetDTO = new UpdateSnippetDTO();
        updateSnippetDTO.newName = "updatedSnippet";
        updateSnippetDTO.content = "updated content";

        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.id = 1L;
        snippetDTO.userId = UUID.randomUUID();
        snippetDTO.name = "updatedSnippet";
        snippetDTO.content = "updated content";

        when(snippetService.updateSnippet(snippetDTO.userId, "testSnippet", updateSnippetDTO.newName, updateSnippetDTO.content))
                .thenReturn(new ResponseEntity<>(snippetDTO, HttpStatus.OK));

        ResponseEntity<SnippetDTO> response = snippetController.updateSnippet(snippetDTO.userId, "testSnippet", updateSnippetDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("updatedSnippet", response.getBody().name);
    }

    @Test
    public void testDeleteSnippet() {
        UUID userId = UUID.randomUUID();
        when(snippetService.deleteSnippet(userId, "testSnippet"))
                .thenReturn(new ResponseEntity<>("204 No Content", HttpStatus.NO_CONTENT));

        ResponseEntity<String> response = snippetController.deleteSnippet("testSnippet", userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("204 No Content", response.getBody());
    }

    @Test
    public void testUpdateStatus() {
        SnippetStatusInputDTO status = new SnippetStatusInputDTO();
        status.status = "PENDING";
        UUID userId = UUID.randomUUID();

        when(snippetService.updateSnippetStatus(userId, "testSnippet", SnippetStatus.PENDING))
                .thenReturn(new ResponseEntity<>("200 OK", HttpStatus.OK));

        ResponseEntity<String> response = snippetController.updateSnippetStatus(userId, "testSnippet", status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("200 OK", response.getBody());

        status.status = "NOT_COMPLIANT";

        when(snippetService.updateSnippetStatus(userId, "testSnippet", SnippetStatus.NOT_COMPLIANT))
                .thenReturn(new ResponseEntity<>("200 OK", HttpStatus.OK));

        response = snippetController.updateSnippetStatus(userId, "testSnippet", status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("200 OK", response.getBody());

        status.status = "COMPLIANT";

        when(snippetService.updateSnippetStatus(userId, "testSnippet", SnippetStatus.COMPLIANT))
                .thenReturn(new ResponseEntity<>("200 OK", HttpStatus.OK));

        response = snippetController.updateSnippetStatus(userId, "testSnippet", status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("200 OK", response.getBody());

        status.status = "INVALID";

        response = snippetController.updateSnippetStatus(userId, "testSnippet", status);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetUserSnippets() {
        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.id = 1L;
        snippetDTO.userId = UUID.randomUUID();
        snippetDTO.name = "testSnippet";
        snippetDTO.content = "snippet content";

        when(snippetService.getUserSnippets(snippetDTO.userId))
                .thenReturn(List.of(snippetDTO));

        ResponseEntity<List<SnippetDTO>> response = snippetController.getUserSnippets(snippetDTO.userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("testSnippet", response.getBody().get(0).name);
    }
}
