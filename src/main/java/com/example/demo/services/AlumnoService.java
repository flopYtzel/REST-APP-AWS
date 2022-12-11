package com.example.demo.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.models.AlumnoModel;
import com.example.demo.repositories.AlumnoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlumnoService {
    @Autowired
    AlumnoRepository alumnoRepository;

    String key = "ASIA5BPQN3VS5JK52Y2R";
    String secret = "l3Ub0P44PVg4ONl7e4L9C13pi5ILiZQ/eQH8tvnK";
    String sessionToken = "FwoGZXIvYXdzEGoaDOeBPhZt9OOfnUdVvSLQAexjarDzebU/WHOB3v935jg5e4OrIOydPSYdBNM8pnt7KWNMlstMX9H4hkNrQyGSyV0pSpo3CS+peVpkTEqxHliZdlV4ROOi8/akMoO96x1UhCKEUfF6QLrMTlcxL9bOtFMl7dHtP3uJL0RQs9WckIMsWUEz9Z6aXbt7LyPXxvbUIgEHkdaqc5GXK9pKOwkg6rkNG59tTd3r8DjKGTwDOX0eNYRjSgzXe+8teE+LBRe0GgZY4IJikXCgzp8vYRIPkmm7cSmgukGmUwzgAhVirN0o2NbUnAYyLVLn4ksC92GTMiUkTQG4YAIYhLt8brdo2uTc83NBPkrWnDurWbfAlKuE1RHx1w==";
    String bucket = "demospringboots3";
    // private String US_EAST_1;

    public AlumnoModel subirFotoPerfil(int id, MultipartFile foto) throws IOException {
        ArrayList<AlumnoModel> alumnoarray = new ArrayList();
        alumnoarray = alumnoRepository.findById(id);
        AlumnoModel alumno = alumnoarray.get(0);

        BasicSessionCredentials awsCreds = new BasicSessionCredentials(key, secret, sessionToken);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_EAST_1).build();
        String key = "fotos/" + alumno.getMatricula() + "_" + alumno.getNombres() + "_fotoPerfil_"
                + foto.getOriginalFilename();

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, foto.getInputStream(),
                new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(putObjectRequest);

        alumno.setFotoPerfilUrl("https://" + bucket + ".s3.amazonaws.com/" + key);

        return alumnoRepository.save(alumno);
    }

    public ArrayList<AlumnoModel> obtenerAlumnos() {
        return (ArrayList<AlumnoModel>) alumnoRepository.findAll();
    }

    public AlumnoModel guardarAlumno(AlumnoModel alumno) {
        return alumnoRepository.save(alumno);
    }

    public AlumnoModel modificarAlumno(AlumnoModel alumno) {
        return alumnoRepository.save(alumno);
    }

    public Optional<AlumnoModel> obtenerPorId(Integer id) {
        return alumnoRepository.findById(id);
    }

    public boolean eliminarAlumno(Integer id) {
        try {
            alumnoRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }
}
