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

    String key = "ASIA5BPQN3VS4BJT7HT7";
    String secret = "KoXCQZ5VrjtyEf7uZuIZKL8nAjZLovxJXEfIJgjV";
    String sessionToken = "FwoGZXIvYXdzELL//////////wEaDBS/nj9R+vMOMQ57xiLQAXFvJ99MrZfmVfJB+mF97OInq5JYXK1kplupagjqXbrXpXRDsQ5hhvnIuZX0oodquMN1kfgZXanxgZlnMXLHz1hdE5BU5ST6TfzAcBqeIUqPJHJIELPV0P82AoDfOtYFRP3n51CrNP5XTrFSbnQU+I684SGJP7MMLdteNmkarr9BeGSy0BYpE0c3pCNTQD7Ap+W9RiLfaSJbXzk7+9KVz8MljPMSzEEhNWKMSva9nxbR6OkSLlpZfA8ONuYcOW1BGiCvI580QSyO4CGyHzzftywop67knAYyLbdlEgPjLLIF8pnnERJKITIKJVzSKlM88zqXclEcNd6xh2p9aKCSJqTwO4OYug==";
    String bucket = "springboots3bucket";
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
