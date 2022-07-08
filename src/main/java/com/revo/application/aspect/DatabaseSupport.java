package com.revo.application.aspect;

import com.revo.domain.Area;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.units.qual.A;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
class DatabaseSupport {

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

    @Before("execution(* com.revo.application.database.file.AreaFileRepository.save(..))")
    public void beforeDatabaseException(JoinPoint joinPoint){
        Arrays.stream(joinPoint.getArgs()).forEach(argument -> {
            if (argument instanceof Area){
                areas.add((Area) argument);
            }
        });
    }

}
