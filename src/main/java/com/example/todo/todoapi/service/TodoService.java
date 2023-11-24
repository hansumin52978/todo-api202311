package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.Todo;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoListResponseDTO create(final TodoCreateRequestDTO requestDTO)
                throws RuntimeException { // 매개변수 앞에 final을 붙이면 값을 변경할 수 없다. 참고만 한다는 뜻
        todoRepository.save(requestDTO.toEntity());
        log.info("할 일 저장 완료! 제목: {}", requestDTO.getTitle());

        return retrieve();
    }

    public TodoListResponseDTO retrieve() {
        List<Todo> entityList = todoRepository.findAll(); // 전체 목록을 조회

        List<TodoDetailResponseDTO> dtoList
                = entityList.stream()
                /*.map(todo -> new TodoDetailResponseDTO(todo))*/
                .map(TodoDetailResponseDTO::new)
                .collect(Collectors.toList());

        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }


    public TodoListResponseDTO delete(final String todoId) {
        try {
            todoRepository.deleteById(todoId);
        } catch (Exception e) {
            log.error("id가 존재하지 않아서 삭제에 실패했습니다. - ID: {}, err: {}"
                    , todoId, e.getMessage());
            throw new RuntimeException("id가 존재하지 않아서 삭제에 실패했습니다.");
        }
        return retrieve();
    }

    public TodoListResponseDTO update(final TodoModifyRequestDTO requestDTO)
        throws RuntimeException {
        Optional<Todo> targetEntity
                = todoRepository.findById(requestDTO.getId());

        targetEntity.ifPresent(todo -> {
            todo.setDone(requestDTO.isDone());

            todoRepository.save(todo);
        });

        return retrieve();
    }
}
