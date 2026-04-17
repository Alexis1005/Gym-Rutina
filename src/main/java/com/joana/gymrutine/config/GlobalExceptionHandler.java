package com.joana.gymrutine.config;

import com.joana.gymrutine.exception.DuplicateEntityException;
import com.joana.gymrutine.exception.EntityNotDeletableException;
import com.joana.gymrutine.exception.EntityNotFoundException;
import com.joana.gymrutine.exception.InvalidOperationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ModelAndView redirectWithError(
            String message,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error", message);

        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer= "/";
        }
        return new ModelAndView("redirect:" + referer);
    }

    /**
     * Maneja excepciones cuando una entidad no puede ser eliminada
     * Redirige a la página anterior con el mensaje de error
     */
    @ExceptionHandler(EntityNotDeletableException.class)
    public ModelAndView handleEntityNotDeletableException(
            EntityNotDeletableException ex,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

       return redirectWithError(ex.getMessage(), request, redirectAttributes);
    }

    /**
     * Maneja excepciones cuando una operación es inválida
     */
    @ExceptionHandler(InvalidOperationException.class)
    public ModelAndView handleInvalidOperationException(
            InvalidOperationException ex,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

       return redirectWithError(ex.getMessage(), request, redirectAttributes);
    }


    /**
    * Maneja duplicados
    * */

    @ExceptionHandler(DuplicateEntityException.class)
    public ModelAndView handleDuplicateEntityException(
            DuplicateEntityException ex,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

       return redirectWithError(ex.getMessage(), request, redirectAttributes);
    }

    /**
     * Maneja excepciones cuando una entidad no es encontrada
     * Redirige a página 404
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(
            EntityNotFoundException ex,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    /**
     * Maneja cualquier excepción no capturada
     * Redirige a página 500
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(
            Exception ex,
            RedirectAttributes redirectAttributes) {

        ex.printStackTrace(); // Log en consola (en producción usar Logger)
        redirectAttributes.addFlashAttribute("error",
                "Ocurrió un error inesperado. Por favor, intenta de nuevo.");

        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }
}