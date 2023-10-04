package com.insper.partida.equipe;

import com.insper.partida.equipe.dto.SaveTeamDTO;
import com.insper.partida.equipe.dto.TeamReturnDTO;
import com.insper.partida.equipe.exception.TeamAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTests {

    @InjectMocks
    TeamService teamService;

    @Mock
    TeamRepository teamRepository;

    @Test
    void test_listTeams() {
        Mockito.when(teamRepository.findAll()).thenReturn(new ArrayList<>());

        List<TeamReturnDTO> resp = teamService.listTeams();

        Assertions.assertEquals(0, resp.size());
    }

    @Test
    void test_listTeamsNotEmpty() {
        Team team = getTeam();

        List<Team> lista = new ArrayList<>();
        lista.add(team);

        Mockito.when(teamRepository.findAll()).thenReturn(lista);

        List<TeamReturnDTO> resp = teamService.listTeams();

        Assertions.assertEquals(1, resp.size());
    }
    
    @Test
    void test_saveTeamAlreadyExists() {
        SaveTeamDTO saveTeamDTO = new SaveTeamDTO();
        saveTeamDTO.setName("Team 3");
        saveTeamDTO.setIdentifier("team-3");

        Mockito.when(teamRepository.existsByIdentifier(saveTeamDTO.getIdentifier())).thenReturn(true);

        Assertions.assertThrows(TeamAlreadyExistsException.class, () -> {
            teamService.saveTeam(saveTeamDTO);
        });
    }

    @Test
    void test_deleteTeam() {
        String identifier = "team-1";

        Team team = getTeam();

        Mockito.when(teamRepository.findByIdentifier(identifier)).thenReturn(team);

        teamService.deleteTeam(identifier);

        Mockito.verify(teamRepository, Mockito.times(1)).delete(team);
    }

    @Test
    void test_getTeam() {
        String identifier = "team-1";

        Team team = getTeam();

        Mockito.when(teamRepository.findByIdentifier(identifier)).thenReturn(team);

        TeamReturnDTO resp =  TeamReturnDTO.covert(teamService.getTeam(identifier));

        Assertions.assertNotNull(resp);
        Assertions.assertEquals("Time 1", resp.getName());
    }

    private static Team getTeam() {
        Team team = new Team();
        team.setId("1");
        team.setIdentifier("team-1");
        team.setName("Time 1");
        return team;
    }
}
