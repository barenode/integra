import * as React from 'react';
import styled from 'styled-components';
import type { Span } from '../../model';    
import SpanDetailComponent from '../SpanDetailComponent';
import emptyImg from './img/empty.gif';
import minusImg from './img/minus.gif';
import plusImg from './img/plus.gif';

const ReportSpanContainer = styled.div`
    flex-grow: 1;
    display: flex;
    flex-direction: column;
`;

const ReportSpan = styled.div`
    min-height: 2em; 
    display: flex; 
    //selected
    // background-color: rgb(35, 44, 55);

    // error
    background-color: rgb(255, 0, 133);
    //error selected
    //background-color: rgb(255, 26, 145);



    cursor: pointer;
    flex-grow: 1;
    border-bottom: 1px solid rgb(34, 46, 62);  
`;

const ChildrenContainer = styled.div`
    margin-left: 0.1em; 
    padding-left: 1em; 
    border-left: 2px solid rgba(0, 0, 0, 0.15);
`;

const ExpandButton = styled.button`
    cursor: pointer; 
    color: inherit; 
    font: inherit; 
    outline: inherit; 
    background: transparent; 
    border: none;   
    width: 24px;
`;

const Expanded = styled.span`
    display: inline-block; 
    transition: all 0.1s ease 0s; 
    transform: rotate(90deg);  
`;

const Collapsed = styled.span`
    display: inline-block; 
    transition: all 0.1s ease 0s; 
`;

const Content = styled.code`
    font-size: 0.9em; 
    color: inherit; 
    background: inherit; 
    padding: 0.5em;
`;


interface ILogComponentProps {
    reportId: string;
    span: Span;
}

interface ILogComponentState {
    expanded: boolean;
    open: boolean;
}

const LogComponent: React.FC<ILogComponentProps> = ({
    reportId,
    span
}) => {
    const { 
        serviceName, 
        childSpans, 
        label,
        traceId,
        spanId,
        id 
    } = span;
    const hasChildSpan = childSpans?.length ? true : false;
    const [state, setState] = React.useState<ILogComponentState>({expanded: false, open: false});
    const { expanded, open } = state;
    const expandCallback = React.useCallback(() => {
        console.log('expand');
        setState({ ...state, expanded: !expanded });
    }, [ state, expanded ]);
    const openCallback = React.useCallback(() => {
        console.log('open');
        setState({ ...state, open: !open });
    }, [ state, open ]);
    const expandIcon = hasChildSpan ? ( expanded ? minusImg : plusImg) : emptyImg;
    return (
        <ReportSpanContainer>
            <ReportSpan> 
                <ExpandButton onClick={(e) => { e.stopPropagation(); expandCallback(); }}>
                    {hasChildSpan && <>
                        {expanded ? <Expanded>▶</Expanded> : <Collapsed>▶</Collapsed> }</>
                    }
                </ExpandButton>  
                <Content>
                    {serviceName}
                </Content>   
                {label && <Content>
                    {label.length < 150 ? label : label?.substring(0, 150)}
                </Content>}                  
            </ReportSpan>
            {expanded &&
                <ChildrenContainer>
                    {childSpans?.map((childSpan) => <LogComponent reportId={reportId} span={childSpan} />)}
                </ChildrenContainer>
            }
        </ReportSpanContainer>
    );
  };
  
  export default LogComponent;