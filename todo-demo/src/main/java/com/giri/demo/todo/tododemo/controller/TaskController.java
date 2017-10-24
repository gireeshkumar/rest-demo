package com.giri.demo.todo.tododemo.controller;


import com.giri.demo.todo.tododemo.model.Task;
import com.giri.demo.todo.tododemo.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${app.rest.base}/task")
public class TaskController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Task addTask(@RequestBody String desc){
        logger.debug("Add Task:"+desc);
        System.out.println("Desc: "+desc);

        final long currentuserid = getCurrentUserId();
        final Task newtask = new Task(currentuserid, desc);

        return taskRepository.save(newtask);
    }


    @RequestMapping(value="/", method = RequestMethod.GET)
    public List<Task> findTasks(@RequestParam Map<String, String> params){

        Task exp = new Task();
        exp.setOwnerid(getCurrentUserId());

        if(params.size() > 0){
            System.out.println(params);
            Boolean completedFilter = getBooleanFilter(params, "completed");
            if(completedFilter != null){
                exp.setCompleted(completedFilter);
            }
        }


        return taskRepository.findAll(Example.of(exp));
    }


    private Boolean getBooleanFilter(Map<String, String> params, String param) {
        Boolean filter = null;
        if (params != null) {
            if (params.containsKey((param))) {
                try {
                    filter = Boolean.parseBoolean(params.get(param));
                } catch (Exception e) {
                    e.printStackTrace();
                    filter = null;
                }
            }
        }
        return filter;
    }

    @RequestMapping(value = "/{taskid}", method = RequestMethod.PUT)
    public Task updateTask(@PathVariable("taskid")long taskid, @RequestBody Task task){

        logger.debug("Update Task : "+taskid);
        logger.debug(task.toString());

        final Task orginalTask = taskRepository.getById(taskid);

        orginalTask.setDescription(task.getDescription());
        orginalTask.setCompleted(task.getCompleted());

        taskRepository.save(orginalTask);

        return orginalTask;
    }



    @RequestMapping(value = "/{taskid}", method = RequestMethod.GET)
    public Task getTask(@PathVariable("taskid")long taskid){

        logger.debug("Get task by id :"+ taskid);
        return taskRepository.getById(taskid);
    }


    @RequestMapping(value = "/{taskid}", method=RequestMethod.DELETE)
    public long deleteTask(@PathVariable("taskid")long taskid){
        logger.debug("Delete Task");

        taskRepository.delete(taskid);

        return taskid;
    }


}
