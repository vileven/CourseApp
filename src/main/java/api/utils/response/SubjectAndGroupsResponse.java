package api.utils.response;

import api.models.Group;

import java.util.List;

/**
 * Created by Vileven on 19.05.17.
 */
public class SubjectAndGroupsResponse {
    private final List<Group> groups;
    private final List<SubjectResponse> subjects;

    public SubjectAndGroupsResponse(List<Group> groups, List<SubjectResponse> subjects) {
        this.groups = groups;
        this.subjects = subjects;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<SubjectResponse> getSubjects() {
        return subjects;
    }
}
