package org.example.snippetservice.domains.snippet;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.example.snippetservice.domains.snippet.controller.SnippetController;
import org.example.snippetservice.domains.snippet.dto.CreateSnippetDTO;
import org.example.snippetservice.domains.snippet.dto.SnippetDTO;
import org.example.snippetservice.domains.snippet.dto.UpdateSnippetDTO;
import org.example.snippetservice.domains.snippet.service.SnippetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        createSnippetDTO.userId = 1L;
        createSnippetDTO.name = "testSnippet";
        createSnippetDTO.content = "snippet content";

        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.id = 1L;
        snippetDTO.userId = 1L;
        snippetDTO.name = "testSnippet";
        snippetDTO.content = "snippet content";

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
        when(snippetService.getSnippetByUserIdAndName(1L, "testSnippet"))
                .thenReturn(content);

        ResponseEntity<String> response = snippetController.getSnippet(1L, "testSnippet");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("snippet content", response.getBody());
    }

    @Test
    public void testUpdateSnippet() {
        UpdateSnippetDTO updateSnippetDTO = new UpdateSnippetDTO();
        updateSnippetDTO.newName = "updatedSnippet";
        updateSnippetDTO.content = "updated content";

        SnippetDTO snippetDTO = new SnippetDTO();
        snippetDTO.id = 1L;
        snippetDTO.userId = 1L;
        snippetDTO.name = "updatedSnippet";
        snippetDTO.content = "updated content";

        when(snippetService.updateSnippet(1L, "testSnippet", updateSnippetDTO.newName, updateSnippetDTO.content))
                .thenReturn(new ResponseEntity<>(snippetDTO, HttpStatus.OK));

        ResponseEntity<SnippetDTO> response = snippetController.updateSnippet(1L, "testSnippet", updateSnippetDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("updatedSnippet", response.getBody().name);
    }

    @Test
    public void testDeleteSnippet() {
        when(snippetService.deleteSnippet(1L, "testSnippet"))
                .thenReturn(new ResponseEntity<>("204 No Content", HttpStatus.NO_CONTENT));

        ResponseEntity<String> response = snippetController.deleteSnippet("testSnippet", 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("204 No Content", response.getBody());
    }
}
