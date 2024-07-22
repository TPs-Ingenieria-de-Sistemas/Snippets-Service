package org.example.snippetservice.domains.share;

import jakarta.validation.Valid;
import org.example.snippetservice.domains.snippet.integration.permits.PermitDTO;
import org.example.snippetservice.domains.snippet.integration.permits.SnippetPermissionsApi;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/share")
public class ShareController {
    private final SnippetPermissionsApi permissionsApi;

    public ShareController(SnippetPermissionsApi permissionsApi) {
        this.permissionsApi = permissionsApi;
    }


    @PostMapping("/{fileName}")
    public ResponseEntity<?> share(@PathVariable String fileName, @RequestBody @Valid PermitDTO dto) {
        return this.permissionsApi.sharePermissions(fileName, dto);
    }

    @DeleteMapping("/{fileName}")
    public  ResponseEntity<?> removeShare(@PathVariable String fileName, @RequestBody @Valid PermitDTO dto) {
        return  this.permissionsApi.removePermissions(fileName, dto);
    }
}
