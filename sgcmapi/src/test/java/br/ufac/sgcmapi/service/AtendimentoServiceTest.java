package br.ufac.sgcmapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ufac.sgcmapi.model.Atendimento;
import br.ufac.sgcmapi.model.EStatus;
import br.ufac.sgcmapi.model.Profissional;
import br.ufac.sgcmapi.repository.AtendimentoRepository;

@ExtendWith(MockitoExtension.class)
public class AtendimentoServiceTest {

    @Mock
    private AtendimentoRepository repo;
    
    @InjectMocks
    private AtendimentoService servico;

    Atendimento a1;
    Atendimento a2;
    List<Atendimento> atendimentos;

    @BeforeEach
    public void setUp() {
        a1 = new Atendimento();
        a2 = new Atendimento();
        atendimentos = new ArrayList<>();
        a1.setId(1L);
        a2.setId(2L);
        a1.setHora(LocalTime.of(14, 00));
        a2.setHora(LocalTime.of(15, 00));
        atendimentos.add(a1);
        atendimentos.add(a2);
    }

    @Test
    void testDelete() {
        Mockito.doNothing().when(repo).deleteById(1L);
        repo.deleteById(1L);
        verify(repo, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testAtendimentoGetAll() {
        Mockito.when(repo.findAll()).thenReturn(atendimentos);
        List<Atendimento> result = servico.get();
        assertEquals(2, result.size());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void testGetAtendimentoById() {
        Long id = 1L;
        Mockito.when(repo.findById(id)).thenReturn(Optional.of(a1));
        Atendimento result = servico.get(id);
        assertEquals(id, result.getId());
    
        Long invalidId = 99L;
        Mockito.when(repo.findById(invalidId)).thenReturn(Optional.empty());
        result = servico.get(invalidId);
        assertEquals(null, result);
    }

    @Test
    void testGetAtendimentoTermoBusca() {
        String termo = "someSearchTerm";
        Mockito.when(repo.busca(termo)).thenReturn(atendimentos);
        List<Atendimento> result = servico.get(termo);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    
        String noResultTerm = "noResultTerm";
        Mockito.when(repo.busca(noResultTerm)).thenReturn(new ArrayList<>());
        result = servico.get(noResultTerm);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAtendimentoSave() {
        Atendimento newAtendimento = new Atendimento();
        newAtendimento.setId(3L);
        Mockito.when(repo.save(newAtendimento)).thenReturn(newAtendimento);
        Atendimento result = servico.save(newAtendimento);
        assertEquals(3L, result.getId());
    
        Mockito.when(repo.save(null)).thenThrow(new IllegalArgumentException("Atendimento não pode ser nulo"));
        try {
            servico.save(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Atendimento não pode ser nulo", e.getMessage());
        }
    }

    @Test
    void testGetHorarios() {
        Mockito.when(repo.findByProfissionalAndDataAndStatusNot(
            Mockito.any(Profissional.class), Mockito.eq(LocalDate.now()), Mockito.eq(EStatus.CANCELADO))).thenReturn(atendimentos);
        List<String> result = servico.getHorarios(1L, LocalDate.now());
        assertEquals(2, result.size());
        assertTrue(result.contains("15:00:00"));
    }

    @Test
    void testUpdateStatus() {
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(a1));
        Atendimento result = servico.updateStatus(1L);
        assertEquals(EStatus.CONFIRMADO, result.getStatus());
    }
}
