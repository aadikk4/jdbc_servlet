package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dormitory {
    private long id;
    private String name;
    private List<Student> students;
}
