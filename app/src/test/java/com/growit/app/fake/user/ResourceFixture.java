package com.growit.app.fake.user;

import com.growit.app.user.domain.jobrole.JobRole;
import java.util.Arrays;
import java.util.List;

public class ResourceFixture {
  public static List<JobRole> defaultJobRoles() {
    JobRole dev = new JobRole("dev", "개발자");
    JobRole designer = new JobRole("designer", "디자이너");
    JobRole planner = new JobRole("planner", "기획자");

    return Arrays.asList(dev, designer, planner);
  }
}
