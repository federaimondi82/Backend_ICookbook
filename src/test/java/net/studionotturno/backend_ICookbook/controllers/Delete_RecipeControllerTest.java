package net.studionotturno.backend_ICookbook.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Delete_RecipeControllerTest {

    @Test
    void deleteSingleDocument() {
        Delete_RecipeController controller=new Delete_RecipeController();
        assertFalse(controller.deleteSingleDocument("<script>","<script>"));
    }
}