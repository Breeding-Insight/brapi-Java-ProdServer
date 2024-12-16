package org.brapi.test.BrAPITestServer.model.entity.germ;

import io.swagger.model.germ.ParentType;
import jakarta.persistence.*;
import org.brapi.test.BrAPITestServer.model.entity.BrAPIPrimaryEntity;

@Entity
@Table(name="pedigree_edge")
public class PedigreeEdgeEntity extends BrAPIPrimaryEntity{
	@ManyToOne(fetch = FetchType.LAZY)
	private PedigreeNodeEntity thisNode;
	@ManyToOne(fetch = FetchType.LAZY)
	private PedigreeNodeEntity connectedNode;
	@Column
	private ParentType parentType;
	@Column
	private EdgeType edgeType;

    public PedigreeNodeEntity getThisNode() {
        return thisNode;
    }

    public void setThisNode(PedigreeNodeEntity thisNode) {
        this.thisNode = thisNode;
    }

    public EdgeType getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(EdgeType edgeType) {
        this.edgeType = edgeType;
    }

    public PedigreeNodeEntity getConnectedNode() {
        return connectedNode;
    }

    public void setConnectedNode(PedigreeNodeEntity conncetedNode) {
        this.connectedNode = conncetedNode;
    }

    public ParentType getParentType() {
        return parentType;
    }

    public void setParentType(ParentType parentType) {
        this.parentType = parentType;
    }

    public enum EdgeType {
        parent, child, sibling
    }
}
