package com.hris.swagger

import com.hris.dto.HierarchyNode
import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.ktor.http.*

fun OpenApiRoute.descriptionGetHierarchyById() {
    description =
        "Default hierarchy after login. Shows employee's supervisor, employee and employee's subordinates"
    request {
        pathParameter<Long>("id") {
            description = "Employee Id"
        }

    }
    response {
        HttpStatusCode.OK to {
            body<HierarchyNode> {
                description = "Hierarchy structure"
                example("Hierarchy") {
                    value = HierarchyNode(
                        id = "1",
                        name = "Tyler",
                        surname = "Durden",
                        position = "none",
                        mutableListOf(
                            HierarchyNode(
                                id = "2",
                                name = "Tyler",
                                surname = "Durden",
                                position = "none",
                                mutableListOf(
                                    HierarchyNode(
                                        id = "3",
                                        name = "Tyler",
                                        surname = "Durden",
                                        position = "none",
                                        mutableListOf()
                                    ),
                                    HierarchyNode(
                                        id = "4",
                                        name = "Tyler",
                                        surname = "Durden",
                                        position = "none",
                                        mutableListOf()
                                    )
                                )
                            )
                        )
                    )
                }
            }
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionGetEqualsById() {
    description = "Get list of employees who equal to employee with provided id"
    request {
        pathParameter<Long>("id") {
            description = "Employee Id"
        }

    }
    response {
        HttpStatusCode.OK to {
            body<List<HierarchyNode>> {
                description = "List of employees who equal to employee with provided id"
                example("List of Employees") {
                    value = listOf(
                        HierarchyNode(
                            id = "1",
                            name = "Tyler",
                            surname = "Durden",
                            position = "none"
                        )
                    )
                }
            }
        }
        HttpStatusCode.NoContent to {
            description = "If no equal employees found"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionGetSubordinatesById() {
    description = "Get list of subordinates of employee with provided id"
    request {
        pathParameter<Long>("id") {
            description = "Employee Id"
        }

    }
    response {
        HttpStatusCode.OK to {
            body<List<HierarchyNode>> {
                description = "List of subordinates of employee with provided id"
                example("List of Employees") {
                    value = listOf(
                        HierarchyNode(
                            id = "1",
                            name = "Tyler",
                            surname = "Durden",
                            position = "none"
                        )
                    )
                }
            }
        }
        HttpStatusCode.NoContent to {
            description = "If no subordinates found"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionGetSupervisorById() {
    description = "Get employee's supervisor"
    request {
        pathParameter<Long>("id") {
            description = "Employee Id"
        }

    }
    response {
        HttpStatusCode.OK to {
            body<List<HierarchyNode>> {
                description = "Employee's supervisor"
                example("Supervisor") {
                    value = listOf(
                        HierarchyNode(
                            id = "1",
                            name = "Tyler",
                            surname = "Durden",
                            position = "none"
                        )
                    )
                }
            }
        }
        HttpStatusCode.NoContent to {
            description = "If no supervisor found"
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}

fun OpenApiRoute.descriptionGetHierarchyToHeadById() {
    description = "Get hierarchy from employee to head"
    request {
        pathParameter<Long>("id") {
            description = "Employee Id"
        }

    }
    response {
        HttpStatusCode.OK to {
            body<HierarchyNode> {
                description = "Hierarchy from employee to head"
                example("Hierarchy") {
                    value = listOf(
                        HierarchyNode(
                            id = "1",
                            name = "Tyler",
                            surname = "Durden",
                            position = "none",
                            mutableListOf(
                                HierarchyNode(
                                    id = "2",
                                    name = "Tyler",
                                    surname = "Durden",
                                    position = "none"
                                )
                            )
                        )
                    )
                }
            }
        }
        HttpStatusCode.BadRequest to {
            description = "Invalid id provided"
        }
    }
}