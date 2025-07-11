package com.GIRA.Backend.DTO.response;

import com.GIRA.Backend.DTO.common.UserSummaryResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for file responses.
 * Contains file information with download URL and metadata.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FichierResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the file.
     */
    @JsonProperty("id")
    private String id;

    /**
     * ID of the complaint this file belongs to.
     */
    @JsonProperty("reclamationId")
    private String reclamationId;

    /**
     * Original name of the uploaded file.
     */
    @JsonProperty("nomOriginal")
    private String nomOriginal;

    /**
     * MIME type of the file.
     */
    @JsonProperty("typeMime")
    private String typeMime;

    /**
     * Size of the file in bytes.
     */
    @JsonProperty("taille")
    private Long taille;

    /**
     * When the file was uploaded.
     */
    @JsonProperty("dateUpload")
    private LocalDateTime dateUpload;

    /**
     * Signed URL for downloading the file.
     */
    @JsonProperty("urlTelechargement")
    private String urlTelechargement;

    /**
     * User who uploaded the file.
     */
    @JsonProperty("uploadePar")
    private UserSummaryResponse uploadePar;
} 