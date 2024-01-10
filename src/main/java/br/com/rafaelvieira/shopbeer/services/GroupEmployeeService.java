package br.com.rafaelvieira.shopbeer.services;

import br.com.rafaelvieira.shopbeer.domain.GroupEmployee;
import br.com.rafaelvieira.shopbeer.repository.GroupsRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class GroupEmployeeService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GroupEmployeeService.class);

    private final GroupsRepository groupsRepository;

    public GroupEmployeeService(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    public GroupEmployee create(GroupEmployee entity) {
        return groupsRepository.save(entity);
    }

    public List<GroupEmployee> create(List<GroupEmployee> entities) {
        return groupsRepository.saveAll(entities);
    }

    @Transactional
    public List<GroupEmployee> findAll() {
        List<GroupEmployee> groups = groupsRepository.findAll();
        for (GroupEmployee group : groups) {
            group.getPermissions().size(); // force initialization
        }
        return groups;
    }

//    public List<GroupEmployee> findAll() {
//        return groupsRepository.findAll();
//    }

    public Optional<GroupEmployee> get(Long id) {
        return groupsRepository.findById(id);
    }

    public GroupEmployee update(GroupEmployee entity) {
        return groupsRepository.save(entity);
    }

    public void delete(Long id) {
        groupsRepository.deleteById(id);
    }

    public Page<GroupEmployee> list(Pageable pageable) {
        return groupsRepository.findAll(pageable);
    }

    public Page<GroupEmployee> list(Pageable pageable, Specification<GroupEmployee> filter) {
        return groupsRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) groupsRepository.count();
    }
}
