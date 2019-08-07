package org.coderdreams;


import org.coderdreams.enums.EmployeeDepartmentType;

public class App {

    public static void main(String[] args) {

        for(EmployeeDepartmentType type : EmployeeDepartmentType.VALUES) {
            System.out.println(type.getDescription());
        }
    }

}
