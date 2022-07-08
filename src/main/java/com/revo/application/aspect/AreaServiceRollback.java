package com.revo.application.aspect;

import com.revo.domain.Area;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
class AreaServiceRollback {
    private List<Area> areas;

    @AfterThrowing("execution(* com.revo.application.database.file.AreaFileRepository.save(..))")
    public void afterThrowDatabaseException(JoinPoint joinPoint){
        Arrays.stream(joinPoint.getArgs()).forEach(argument -> {
            if (argument instanceof Area){
                Area beforeMethodArea = (Area) argument;
                areas.forEach(targetArea -> {
                    if(targetArea.getName().equals(beforeMethodArea.getName())){
                        rollbackArea(targetArea, beforeMethodArea);
                    }
                });
            }
        });
    }

    private void rollbackArea(Area targetArea, Area beforeMethodArea) {
        targetArea.setName(beforeMethodArea.getName());
        targetArea.setAuthor(beforeMethodArea.getAuthor());
        targetArea.setCheckPoints(beforeMethodArea.getCheckPoints());
        targetArea.setStart(beforeMethodArea.getStart());
        targetArea.setEnd(beforeMethodArea.getEnd());
        targetArea.setFloor(beforeMethodArea.getFloor());
    }

    @Before("execution(* com.revo.application.database.file.AreaFileRepository.updateStartInArea(..))")
    public void beforeDatabaseExceptionInStartUpdate(JoinPoint joinPoint){
        addAreaToMemory(joinPoint);
    }

    @Before("execution(* com.revo.application.database.file.AreaFileRepository.updateEndInArea(..))")
    public void beforeDatabaseExceptionInEndUpdate(JoinPoint joinPoint){
        addAreaToMemory(joinPoint);
    }

    @Before("execution(* com.revo.application.database.file.AreaFileRepository.addCheckPointInArea(..))")
    public void beforeDatabaseExceptionInCheckPointUpdateWhileAdding(JoinPoint joinPoint){
        addAreaToMemory(joinPoint);
    }

    @Before("execution(* com.revo.application.database.file.AreaFileRepository.removeCheckPointInArea(..))")
    public void beforeDatabaseExceptionInCheckPointUpdateWhileRemoving(JoinPoint joinPoint){
        addAreaToMemory(joinPoint);
    }

    private void addAreaToMemory(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs()).forEach(argument -> {
            if (argument instanceof Area){
                Area argumentAsArea = (Area) argument;
                updateInMemory(argumentAsArea);
            }
        });
    }

    private void updateInMemory(Area argumentAsArea) {
        try{
            areas.remove(getFromMemory(argumentAsArea.getName()));
        } catch (IndexOutOfBoundsException exception){
            areas.add(argumentAsArea);
        }
    }

    private Area getFromMemory(String argumentName) {
        return areas.stream()
                .filter(area -> isCurrentArea(area.getName(), argumentName))
                .collect(Collectors.toList())
                .get(0);
    }

    private boolean isCurrentArea(String name, String argumentName) {
        return Objects.equals(name, argumentName);
    }

}
