import * as React from 'react';
import styled from 'styled-components';
import { Severity, Span } from '../../model';    
import { useApplicationContext } from '../../context/applicationState';

const ReportSpanContainer = styled.div`
    flex-grow: 1;
    display: flex;
    flex-direction: column;
`;

const severityColorMap = {
    [Severity.info]: {
        background: "rgb(11, 21, 33)",
        selectedBackground: "rgb(35, 44, 55)"
    },
    [Severity.error]: {
        background: "rgb(255, 0, 133)",
        selectedBackground: "rgb(255, 26, 145)"
    }
}

const ReportSpan = styled.div<{selected: boolean; severity: Severity}>`
    min-height: 2em; 
    display: flex;     
    ${({ severity }) => `
        background-color: ${severityColorMap[severity].background};
    `}
    ${({ selected, severity }) => selected && `
        background-color: ${severityColorMap[severity].selectedBackground};
    `}

    // // error
    // background-color: rgb(255, 0, 133);
    // ${({ selected }) => selected && `
    //     background-color: rgb(255, 26, 145);
    // `}

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

const LogComponent: React.FC<ILogComponentProps> = ({
    reportId,
    span
}) => {
    const { 
        serviceName, 
        childSpans, 
        label,
        id,
        timestamp,
        severity 
    } = span;
    const hasChildSpan = childSpans?.length ? true : false;
    const { state, setState } = useApplicationContext();
    const { selectedSpanId } = state;
    const selected = selectedSpanId === id;
    const [expanded, setExpanded] = React.useState<boolean>(false);    
    const selectCallback = React.useCallback(() => {
        console.log('open');
        setState({ ...state, selectedSpanId: id });
    }, [ state ]);
    return (
        <ReportSpanContainer>
            <ReportSpan severity={severity || Severity.info} selected={selected} onClick={selectCallback}> 
                <ExpandButton onClick={(e) => { e.stopPropagation(); setExpanded(!expanded); }}>
                    {hasChildSpan && <>
                        {expanded ? <Expanded>▶</Expanded> : <Collapsed>▶</Collapsed> }</>
                    }
                </ExpandButton>  
                <Content>
                    {timestamp}
                </Content>   
                <Content>
                    {serviceName}
                </Content>   
                {label && <Content>
                    {label.length < 150 ? label : label?.substring(0, 150)}
                </Content>}                  
            </ReportSpan>
            {expanded &&
                <ChildrenContainer>
                    {childSpans?.map((childSpan) => <LogComponent key={childSpan.id} reportId={reportId} span={childSpan} />)}
                </ChildrenContainer>
            }
        </ReportSpanContainer>
    );
  };
  
  export default LogComponent;